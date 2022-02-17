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
    private val subscribeMachine: SubscribeMachine = subscribeMachine(
        shouldRetry = { it < 2 }
    ),
    private val effectsQueue: LinkedBlockingQueue<Effect> = LinkedBlockingQueue(100),
    private val threadExecutor: ExecutorService = Executors.newFixedThreadPool(2),
    private val effects: MutableMap<String, () -> Unit> = mutableMapOf(),
    private val httpEffectExecutor: EffectExecutor<SubscribeHttpEffect> = HttpCallExecutor(
        callsExecutor,
        eventQueue = inputQueue
    ),
    private val retryEffectExecturor: EffectExecutor<ScheduleRetry> = RetryEffectExecutor(effectQueue = effectsQueue)
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
                        is SubscribeHttpEffect -> effects[effect.id] = httpEffectExecutor.execute(effect)
                        is NewState -> logger.info("New state: ${effect.name}")
                        is NewMessages -> logger.info(
                            "New messages. Hopefully they're fine ;) ${effect.messages}"
                        )
                        is ScheduleRetry -> effects[effect.id] = retryEffectExecturor.execute(effect)

                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    logger.trace("take message interrupted!", e)
                }
            }

        }, threadExecutor)
    }
}
