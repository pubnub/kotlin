package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.state.EffectExecutor
import com.pubnub.api.state.LongRunningEffectsTracker
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference

internal class PresenceModuleInternals private constructor(
    private val status: AtomicReference<PresenceStatus>,
    private val eventQueue: LinkedBlockingQueue<PresenceEvent>,
    private val presenceMachine: PresenceMachine,
    private val effectsQueue: LinkedBlockingQueue<PresenceEffect>,
    private val executorService: ExecutorService,
    private val longRunningEffectsTracker: LongRunningEffectsTracker,
    private val httpExecutor: EffectExecutor<PresenceHttpEffect>,
    private val scheduledExecutorService: ScheduledExecutorService
) {
    private lateinit var handleEffects: CompletableFuture<Void>
    private lateinit var handleEventsLoop: CompletableFuture<Void>
    private val logger = LoggerFactory.getLogger(PresenceModuleInternals::class.java)


    companion object {
        fun create(
            pubnub: PubNub,
            eventQueue: LinkedBlockingQueue<PresenceEvent> = LinkedBlockingQueue(100),
            presMachine: PresenceMachine = presenceMachine(),
            effectsQueue: LinkedBlockingQueue<PresenceEffect> = LinkedBlockingQueue(100),
            executorService: ExecutorService = Executors.newFixedThreadPool(2),
            longRunningEffectsTracker: LongRunningEffectsTracker = LongRunningEffectsTracker(),
            httpExecutor: EffectExecutor<PresenceHttpEffect> = HttpCallExecutor(
                pubNub = pubnub,
                eventQueue = eventQueue
            ),
            scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
        ): PresenceModuleInternals {

            val effects = presMachine(InitialEvent)

            return PresenceModuleInternals(
                status = AtomicReference(),
                eventQueue = eventQueue,
                presenceMachine = presMachine,
                effectsQueue = effectsQueue,
                longRunningEffectsTracker = longRunningEffectsTracker,
                executorService = executorService,
                httpExecutor = httpExecutor,
                scheduledExecutorService = scheduledExecutorService
            ).apply {
                effects.forEach {
                    processSingleEffect(it)
                }
                run()
            }
        }

    }

    fun handleEvent(event: PresenceEvent) {
        eventQueue.put(event)
    }

    fun status(): PresenceStatus {
        return status.get()
    }

    private fun <T> LinkedBlockingQueue<T>.waitAndProcess(handle: (T) -> Unit): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                try {
                    handle(take())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    logger.trace("take message interrupted!", e)
                }
            }

        }, executorService)

    }

    private fun processSingleEvent(event: PresenceEvent) {
        val effects = presenceMachine(event)
        effects.forEach { effectsQueue.put(it) }
    }

    private fun processSingleEffect(effect: PresenceEffect) {
        when (effect) {
            is CancelEffect -> {}
            is PresenceHttpEffect -> longRunningEffectsTracker.track(effect) {
                httpExecutor.execute(effect)
            }

            is NewState -> {}
            is TimerEffect -> longRunningEffectsTracker.track(effect) {
                scheduledExecutorService.schedule({
                    eventQueue.put(effect.event)
                }, 1000, TimeUnit.MILLISECONDS).let {
                    { it.cancel(true) }
                }
            }
        }
    }

    private fun run() {
        handleEventsLoop = eventQueue.waitAndProcess { processSingleEvent(it) }

        handleEffects = effectsQueue.waitAndProcess { processSingleEffect(it) }
    }
}