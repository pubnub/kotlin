package com.pubnub.api.subscribe.internal

import com.pubnub.api.network.CallsExecutor
import com.pubnub.api.state.Effect
import com.pubnub.api.subscribe.*
import com.pubnub.api.subscribe.internal.SubscribeHttpEffect.HandshakeHttpCallEffect
import com.pubnub.api.subscribe.internal.SubscribeHttpEffect.ReceiveMessagesHttpCallEffect
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

internal class SubscribeModule(
    private val callsExecutor: CallsExecutor,
    private val inputQueue: LinkedBlockingQueue<SubscribeInput>,
    private val subscribeMachine: SubscribeMachine = SubscribeMachine(),
    private val effectsQueue: LinkedBlockingQueue<Effect> = LinkedBlockingQueue(100),
    private val threadExecutor: ExecutorService = Executors.newFixedThreadPool(2)
) {

    private val logger = LoggerFactory.getLogger(SubscribeModule::class.java)

    fun run() {
        val handleInputs = CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                try {
                    val input = inputQueue.take()
                    val effects = subscribeMachine.handle(input)
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
                        is EndHttpCallEffect -> callsExecutor.cancel(effect.idToCancel)
                        is HandshakeHttpCallEffect -> callsExecutor.handshake(
                            id = effect.id,
                            channels = effect.subscribeStateBag.channels,
                            channelGroups = effect.subscribeStateBag.groups
                        ) { r, s ->
                            if (!s.error) {
                                inputQueue.put(
                                    HandshakeResult.HandshakeSuccess(
                                        Cursor(
                                            timetoken = r!!.metadata.timetoken, //TODO we could improve callback to avoid !! here
                                            region = r.metadata.region
                                        )
                                    )
                                )
                            } else {
                                inputQueue.put(
                                    HandshakeResult.HandshakeFail
                                )
                            }
                        }
                        is ReceiveMessagesHttpCallEffect -> callsExecutor.receiveMessages(
                            id = effect.id,
                            channels = effect.subscribeStateBag.channels,
                            channelGroups = effect.subscribeStateBag.groups,
                            timetoken = effect.subscribeStateBag.cursor!!.timetoken, //TODO figure out how to drop !! here
                            region = effect.subscribeStateBag.cursor.region
                        ) { r, s ->
                            if (!s.error) {
                                inputQueue.put(
                                    ReceivingResult.ReceivingSuccess(
                                        r!!
                                    )
                                )
                            } else {
                                inputQueue.put(
                                    ReceivingResult.ReceivingFail
                                )
                            }
                        }
                        is NewStateEffect -> logger.info("New state: ${effect.name}")
                        is NewMessagesEffect -> logger.info(
                            "New messages. Hopefully they're fine ;) ${effect.messages}"
                        )
                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    logger.trace("take message interrupted!", e)
                }
            }

        }, threadExecutor)
    }
}