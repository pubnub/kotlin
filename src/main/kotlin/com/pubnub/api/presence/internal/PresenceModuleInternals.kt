package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.state.EffectExecutor
import com.pubnub.api.state.LongRunningEffectsTracker
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicReference

class PresenceModuleInternals(
    pubNub: PubNub,
    private val status: AtomicReference<PresenceStatus>,
    private val eventQueue: LinkedBlockingQueue<PresenceEvent>,
    private val presenceMachine: PresenceMachine,
    private val effectsQueue: LinkedBlockingQueue<PresenceEffect>,
    private val executorService: ExecutorService,
    private val longRunningEffectsTracker: LongRunningEffectsTracker,
    private val httpExecutor: EffectExecutor<PresenceHttpEffect> = HttpCallExecutor(
        pubNub = pubNub,
        eventQueue = eventQueue
    )
) {
    private lateinit var handleEffects: CompletableFuture<Void>
    private lateinit var handleEventsLoop: CompletableFuture<Void>
    private val logger = LoggerFactory.getLogger(PresenceModuleInternals::class.java)


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
            is CancelEffect -> TODO()
            is PresenceHttpEffect -> longRunningEffectsTracker.track(effect) {
                httpExecutor.execute(effect)
            }

            is NewState -> TODO()
            is TimerEffect -> TODO()
        }
    }

    private fun run() {
        handleEventsLoop = eventQueue.waitAndProcess { processSingleEvent(it) }

        handleEffects = effectsQueue.waitAndProcess { processSingleEffect(it) }
    }
}