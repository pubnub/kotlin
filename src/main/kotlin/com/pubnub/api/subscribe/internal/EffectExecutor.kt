package com.pubnub.api.subscribe.internal

import com.pubnub.api.network.CallsExecutor
import com.pubnub.api.state.Effect
import com.pubnub.api.subscribe.HandshakeResult
import com.pubnub.api.subscribe.ReceivingResult
import com.pubnub.api.subscribe.SubscribeEvent
import java.util.concurrent.*

interface EffectExecutor<EF : Effect> {
    fun execute(effect: EF, longRunningEffectDone: (String) -> Unit = {}): CancelFn
}

typealias CancelFn = () -> Unit

internal class HttpCallExecutor(
    private val callsExecutor: CallsExecutor,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>
) : EffectExecutor<SubscribeHttpEffect> {

    override fun execute(effect: SubscribeHttpEffect, longRunningEffectDone: (String) -> Unit): CancelFn {
        return when (effect) {
            is SubscribeHttpEffect.HandshakeHttpCallEffect -> {
                callsExecutor.handshake(
                    id = effect.id,
                    channels = effect.subscriptionStatus.channels,
                    channelGroups = effect.subscriptionStatus.groups
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
                            HandshakeResult.HandshakeFailed
                        }
                    )
                }.let { { it.cancelable.silentCancel() } }
            }
            is SubscribeHttpEffect.ReceiveMessagesHttpCallEffect -> callsExecutor.receiveMessages(
                id = effect.id,
                channels = effect.subscriptionStatus.channels,
                channelGroups = effect.subscriptionStatus.groups,
                timetoken = effect.subscriptionStatus.cursor!!.timetoken, //TODO figure out how to drop !! here
                region = effect.subscriptionStatus.cursor.region
            ) { r, s ->
                longRunningEffectDone(effect.id)
                eventQueue.put(
                    if (!s.error) {
                        ReceivingResult.ReceivingSucceeded(r!!)
                    } else {
                        ReceivingResult.ReceivingFailed
                    }
                )
            }.let { { it.cancelable.silentCancel() } }
        }
    }
}

internal class RetryEffectExecutor(
    private val effectQueue: LinkedBlockingQueue<Effect>,
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(3)
) : EffectExecutor<ScheduleRetry> {
    override fun execute(effect: ScheduleRetry, longRunningEffectDone: (String) -> Unit): CancelFn {
        return executor.schedule(Callable {
            longRunningEffectDone(effect.id)
            effectQueue.put(effect.retryableEffect)
        }, effect.retryCount.toLong(), TimeUnit.SECONDS).let {
            {
                it.cancel(true)
            }
        }
    }
}
