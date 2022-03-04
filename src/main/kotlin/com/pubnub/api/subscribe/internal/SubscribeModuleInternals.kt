package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.presence.internal.HttpCallExecutor
import com.pubnub.api.presence.internal.IncomingPayloadProcessor
import com.pubnub.api.presence.internal.NewMessagesEffectExecutor
import com.pubnub.api.presence.internal.RetryEffectExecutor
import com.pubnub.api.state.EffectExecutor
import com.pubnub.api.state.LongRunningEffectsTracker
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicReference

internal class SubscribeModuleInternals private constructor(
    private val status: AtomicReference<SubscriptionStatus>,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>,
    private val subscribeMachine: SubscribeMachine,
    private val effectsQueue: LinkedBlockingQueue<SubscribeEffect>,
    private val executorService: ExecutorService,
    private val longRunningEffectsTracker: LongRunningEffectsTracker,
    private val httpEffectExecutor: EffectExecutor<SubscribeHttpEffect>,
    private val retryEffectExecutor: EffectExecutor<ScheduleRetry>,
    private val newMessagesEffectExecutor: EffectExecutor<NewMessages>,
    private val newStateEffectExecutor: EffectExecutor<NewState>
) {

    companion object {
        fun create(
            pubNub: PubNub,
            incomingPayloadProcessor: IncomingPayloadProcessor,
            listenerManager: ListenerManager,
            eventQueue: LinkedBlockingQueue<SubscribeEvent> = LinkedBlockingQueue(100),
            effectsQueue: LinkedBlockingQueue<SubscribeEffect> = LinkedBlockingQueue(100),
            retryPolicy: RetryPolicy = ExponentialPolicy(),
            subscrMachine: SubscribeMachine = subscribeMachine(
                shouldRetry = retryPolicy::shouldRetry
            ),
            longRunningEffectsTracker: LongRunningEffectsTracker = LongRunningEffectsTracker(),
            httpEffectExecutor: EffectExecutor<SubscribeHttpEffect> = HttpCallExecutor(
                pubNub = pubNub, eventQueue = eventQueue
            ),
            retryEffectExecutor: EffectExecutor<ScheduleRetry> = RetryEffectExecutor(
                effectQueue = effectsQueue, retryPolicy = retryPolicy
            ),
            executorService: ExecutorService = Executors.newFixedThreadPool(2),
            newMessagesEffectExecutor: EffectExecutor<NewMessages> = NewMessagesEffectExecutor(incomingPayloadProcessor),
            newStateEffectExecutor: EffectExecutor<NewState> = NewStateEffectExecutor(listenerManager)
        ): SubscribeModuleInternals {

            val effects = subscrMachine(InitialEvent)

            return SubscribeModuleInternals(
                status = AtomicReference(),
                eventQueue = eventQueue,
                subscribeMachine = subscrMachine,
                effectsQueue = effectsQueue,
                longRunningEffectsTracker = longRunningEffectsTracker,
                httpEffectExecutor = httpEffectExecutor,
                retryEffectExecutor = retryEffectExecutor,
                executorService = executorService,
                newMessagesEffectExecutor = newMessagesEffectExecutor,
                newStateEffectExecutor = newStateEffectExecutor
            ).apply {
                effects.forEach {
                    processSingleEffect(it)
                }
                run()
            }
        }
    }


    private val logger = LoggerFactory.getLogger(SubscribeModuleInternals::class.java)
    private lateinit var handleEventsLoop: CompletableFuture<Void>
    private lateinit var handleEffects: CompletableFuture<Void>

    fun handleEvent(event: SubscribeEvent) {
        eventQueue.put(event)
    }

    fun status(): SubscriptionStatus {
        return status.get()
    }

    private fun processSingleEvent(event: SubscribeEvent) {
        val effects = subscribeMachine(event)
        effects.forEach { effectsQueue.put(it) }
    }

    private fun <T> LinkedBlockingQueue<T>.waitAndProcess(handle: (T) -> Unit) {
        try {
            handle(take())
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            logger.trace("take message interrupted!", e)
        }
    }

    private fun processSingleEffect(effect: SubscribeEffect) = when (effect) {
        is CancelEffect -> longRunningEffectsTracker.cancel(effect.idToCancel)
        is SubscribeHttpEffect -> longRunningEffectsTracker.track(effect) {
            httpEffectExecutor.execute(
                effect, longRunningEffectDone = longRunningEffectsTracker::stopTracking
            )
        }
        is NewState -> {
            status.set(effect.status)
            newStateEffectExecutor.execute(effect)
            logger.info("New state: $effect")
        }
        is NewMessages -> {
            newMessagesEffectExecutor.execute(effect)
            logger.info(
                "New messages. Hopefully they're fine ;) ${effect.messages}"
            )
        }
        is ScheduleRetry -> longRunningEffectsTracker.track(effect) {
            retryEffectExecutor.execute(
                effect, longRunningEffectDone = longRunningEffectsTracker::stopTracking
            )
        }
    }

    private fun run() {
        handleEventsLoop = CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                eventQueue.waitAndProcess { processSingleEvent(it) }
            }

        }, executorService)

        handleEffects = CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                effectsQueue.waitAndProcess { processSingleEffect(it) }
            }
        }, executorService)
    }
}
