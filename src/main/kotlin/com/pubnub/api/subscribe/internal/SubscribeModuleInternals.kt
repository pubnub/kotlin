package com.pubnub.api.subscribe.internal

import com.pubnub.api.network.CallsExecutor
import com.pubnub.api.state.EffectExecutor
import com.pubnub.api.state.LongRunningEffectsTracker
import com.pubnub.api.subscribe.*
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicReference

internal class SubscribeModule(
    private val status: AtomicReference<SubscriptionStatus>,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>,
    private val subscribeMachine: SubscribeMachine,
    private val effectsQueue: LinkedBlockingQueue<AbstractSubscribeEffect>,
    private val threadExecutor: ExecutorService,
    private val longRunningEffectsTracker: LongRunningEffectsTracker,
    private val httpEffectExecutor: EffectExecutor<SubscribeHttpEffect>,
    private val retryEffectExecutor: EffectExecutor<ScheduleRetry>
) {

    companion object {
        fun create(
            callsExecutor: CallsExecutor,
            eventQueue: LinkedBlockingQueue<SubscribeEvent> = LinkedBlockingQueue(100),
            effectsQueue: LinkedBlockingQueue<AbstractSubscribeEffect> = LinkedBlockingQueue(100),
            retryPolicy: RetryPolicy = ExponentialPolicy(),
            subscrMachine: SubscribeMachine = subscribeMachine(
                shouldRetry = retryPolicy::shouldRetry
            ),
            longRunningEffectsTracker: LongRunningEffectsTracker = LongRunningEffectsTracker(),
            httpEffectExecutor: EffectExecutor<SubscribeHttpEffect> = HttpCallExecutor(
                callsExecutor, eventQueue = eventQueue
            ),
            retryEffectExecutor: EffectExecutor<ScheduleRetry> = RetryEffectExecutor(
                effectQueue = effectsQueue, retryPolicy = retryPolicy
            ),
            threadExecutor: ExecutorService = Executors.newFixedThreadPool(2)
        ): SubscribeModule {

            val effects = subscrMachine(InitialEvent)

            return SubscribeModule(
                status = AtomicReference(),
                eventQueue = eventQueue,
                subscribeMachine = subscrMachine,
                effectsQueue = effectsQueue,
                longRunningEffectsTracker = longRunningEffectsTracker,
                httpEffectExecutor = httpEffectExecutor,
                retryEffectExecutor = retryEffectExecutor,
                threadExecutor = threadExecutor
            ).apply {
                effects.forEach {
                    processSingleEffect(it)
                }
                run()
            }
        }
    }


    private val logger = LoggerFactory.getLogger(SubscribeModule::class.java)
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

    private fun processSingleEffect(effect: AbstractSubscribeEffect) = when (effect) {
        is CancelEffect -> longRunningEffectsTracker.cancel(effect.idToCancel)
        is SubscribeHttpEffect -> longRunningEffectsTracker.track(effect) {
            httpEffectExecutor.execute(
                effect, longRunningEffectDone = longRunningEffectsTracker::stopTracking
            )
        }
        is NewState -> {
            status.set(effect.status)
            logger.info("New state: $effect")
        }
        is NewMessages -> logger.info(
            "New messages. Hopefully they're fine ;) ${effect.messages}"
        )
        is ScheduleRetry -> longRunningEffectsTracker.track(effect) {
            retryEffectExecutor.execute(
                effect, longRunningEffectDone = longRunningEffectsTracker::stopTracking
            )
        }
        else -> TODO("BUT WHY?!")
    }

    private fun run() {
        handleEventsLoop = CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                eventQueue.waitAndProcess { processSingleEvent(it) }
            }

        }, threadExecutor)

        handleEffects = CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                effectsQueue.waitAndProcess { processSingleEffect(it) }
            }

        }, threadExecutor)
    }
}
