package com.pubnub.api.subscribe.internal

import com.pubnub.api.network.CallsExecutor
import com.pubnub.api.state.Effect
import com.pubnub.api.subscribe.NewMessages
import com.pubnub.api.subscribe.NewState
import com.pubnub.api.subscribe.SubscribeEvent
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

internal class SubscribeModule(
    private val callsExecutor: CallsExecutor,
    private val inputQueue: LinkedBlockingQueue<SubscribeEvent>,
    private val retryPolicy: RetryPolicy = ExponentialPolicy(),
    private val subscribeMachine: SubscribeMachine = subscribeMachine(
        shouldRetry = retryPolicy::shouldRetry
    ),
    private val effectsQueue: LinkedBlockingQueue<Effect> = LinkedBlockingQueue(100),
    private val threadExecutor: ExecutorService = Executors.newFixedThreadPool(2),
    private val longRunningEffectsTracker: LongRunningEffectsTracker = LongRunningEffectsTracker(),
    private val httpEffectExecutor: EffectExecutor<SubscribeHttpEffect> = HttpCallExecutor(
        callsExecutor,
        eventQueue = inputQueue),
    private val retryEffectExecutor: EffectExecutor<ScheduleRetry> = RetryEffectExecutor(effectQueue = effectsQueue, retryPolicy = retryPolicy)
) {

    private val logger = LoggerFactory.getLogger(SubscribeModule::class.java)

    fun run() {
        val handleInputs = CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                try {
                    val input = inputQueue.take()
                    val effects = subscribeMachine(input)
                    effects.forEach { effectsQueue.put(it) }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    logger.trace("take message interrupted!", e)
                }
            }

        }, threadExecutor)

        val handleEffects = CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                try {
                    when (val effect = effectsQueue.take()) {
                        is CancelEffect -> longRunningEffectsTracker.cancel(effect.idToCancel)
                        is SubscribeHttpEffect -> longRunningEffectsTracker.track(effect, httpEffectExecutor.execute(effect, longRunningEffectDone = longRunningEffectsTracker::cancel))
                        is NewState -> logger.info("New state: $effect")
                        is NewMessages -> logger.info(
                            "New messages. Hopefully they're fine ;) ${effect.messages}"
                        )
                        is ScheduleRetry -> longRunningEffectsTracker.track(effect, retryEffectExecutor.execute(effect, longRunningEffectDone = longRunningEffectsTracker::cancel))

                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    logger.trace("take message interrupted!", e)
                }
            }

        }, threadExecutor)
    }
}
