package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.presence.internal.IncomingPayloadProcessor
import com.pubnub.api.state.CancelFn
import com.pubnub.api.state.EffectExecutor
import java.util.concurrent.*

internal class HttpCallExecutor(
    private val pubNub: PubNub,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>
) : EffectExecutor<SubscribeHttpEffect> {

    override fun execute(effect: SubscribeHttpEffect, longRunningEffectDone: (String) -> Unit): CancelFn {
        return when (effect) {
            is SubscribeHttpEffect.HandshakeHttpCallEffect -> {
                pubNub.handshake(
                    channels = effect.subscriptionStatus.channels.toList(),
                    channelGroups = effect.subscriptionStatus.groups.toList()
                ) { r, s ->
                    longRunningEffectDone(effect.id)
                    eventQueue.put(
                        if (!s.error) {
                            HandshakeResult.HandshakeSucceeded(
                                Cursor(
                                    timetoken = r!!.metadata.timetoken, //TODO we could improve callback to avoid !! here
                                    region = r.metadata.region
                                )
                            )
                        } else {
                            HandshakeResult.HandshakeFailed(s)
                        }
                    )
                }.let { { it.silentCancel() } }
            }
            is SubscribeHttpEffect.ReceiveMessagesHttpCallEffect -> pubNub.receiveMessages(
                channels = effect.subscriptionStatus.channels.toList(),
                channelGroups = effect.subscriptionStatus.groups.toList(),
                timetoken = effect.subscriptionStatus.cursor!!.timetoken, //TODO figure out how to drop !! here
                region = effect.subscriptionStatus.cursor.region
            ) { r, s ->
                longRunningEffectDone(effect.id)
                eventQueue.put(
                    if (!s.error) {
                        ReceivingResult.ReceivingSucceeded(r!!)
                    } else {
                        ReceivingResult.ReceivingFailed(s)
                    }
                )
            }.let { { it.silentCancel() } }
        }
    }
}

internal class RetryEffectExecutor(
    private val effectQueue: LinkedBlockingQueue<SubscribeEffect>,
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(3),
    private val retryPolicy: RetryPolicy = NoPolicy
) : EffectExecutor<ScheduleRetry> {
    override fun execute(effect: ScheduleRetry, longRunningEffectDone: (String) -> Unit): CancelFn {
        return executor.schedule(Callable {
            longRunningEffectDone(effect.id)
            effectQueue.put(effect.retryableEffect)
        }, retryPolicy.computeDelay(effect.retryCount).seconds, TimeUnit.SECONDS).let {
            {
                it.cancel(true)
            }
        }
    }
}

internal interface IncomingPayloadProcessor {
    fun processIncomingPayload(message: SubscribeMessage)
}

internal class NewMessagesEffectExecutor(private val processor: IncomingPayloadProcessor) : EffectExecutor<NewMessages> {
    override fun execute(effect: NewMessages, longRunningEffectDone: (String) -> Unit): CancelFn {
        effect.messages.forEach {
            processor.processIncomingPayload(it)
        }
        return {}
    }
}
