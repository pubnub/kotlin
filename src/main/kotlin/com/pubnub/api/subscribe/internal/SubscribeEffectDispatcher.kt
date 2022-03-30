package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass

internal class SubscribeEffectDispatcher(
    private val httpHandler: EffectHandlerFactory<SubscribeHttpEffectInvocation>,
    private val retryEffectExecutor: EffectHandlerFactory<ScheduleRetry>,
    private val newMessagesEffectExecutor: EffectHandlerFactory<NewMessages>,
    private val notificationEffectExecutor: EffectHandlerFactory<NotificationEffect>,
    override val trackedHandlers: MutableMap<String, ManagedEffectHandler> = mutableMapOf()
) : EffectDispatcher<SubscribeEffectInvocation>, EffectTracker {

    override fun dispatch(effect: SubscribeEffectInvocation) {
        val handler = when (effect) {
            is CancelEffectInvocation -> {
                stopTracking(effect.idToCancel)
                null
            }
            is SubscribeHttpEffectInvocation -> httpHandler.handler(effect)
            is NewMessages -> newMessagesEffectExecutor.handler(effect)
            is ScheduleRetry -> retryEffectExecutor.handler(effect)
            is NotificationEffect -> notificationEffectExecutor.handler(effect)
        }
        handler?.start()
        if (handler is ManagedEffectHandler) {
            startTracking(effect.id(), handler)
        }
    }

    fun cancel() {
    }
}

internal class NotificationExecutor(private val listenerManager: ListenerManager) :
    EffectHandlerFactory<NotificationEffect> {
    override fun handler(effect: NotificationEffect): EffectHandler {
        return EffectHandler.create(
            startFn = {
                when (effect) {
                    Connected -> listenerManager.announce(
                        PNStatus(
                            PNStatusCategory.PNConnectedCategory,
                            error = false,
                            operation = PNOperationType.PNSubscribeOperation
                        )
                    )

                    is Disconnected -> listenerManager.announce(
                        PNStatus(
                            PNStatusCategory.PNReconnectionAttemptsExhausted,
                            error = true,
                            operation = PNOperationType.PNSubscribeOperation
                        )
                    )

                    Reconnected -> listenerManager.announce(
                        PNStatus(
                            PNStatusCategory.PNReconnectedCategory,
                            error = false,
                            operation = PNOperationType.PNSubscribeOperation
                        )
                    )

                }
            }
        )
    }
}

internal class HttpCallExecutor(
    private val pubnub: PubNub, private val eventQueue: LinkedBlockingQueue<SubscribeEvent>
) : EffectHandlerFactory<SubscribeHttpEffectInvocation> {

    override fun handler(effect: SubscribeHttpEffectInvocation): EffectHandler {
        return when (effect) {
            is SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation -> {
                EffectHandler.create(startFn = {
                    pubnub.handshake(
                        channels = effect.subscribeExtendedState.channels.toList(),
                        channelGroups = effect.subscribeExtendedState.groups.toList()
                    ) { r, s ->
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
                    }
                }, cancelFn = { silentCancel() })

            }
            is SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation -> {

                EffectHandler.create(startFn = {
                    pubnub.receiveMessages(
                        channels = effect.subscribeExtendedState.channels.toList(),
                        channelGroups = effect.subscribeExtendedState.groups.toList(),
                        timetoken = effect.subscribeExtendedState.cursor!!.timetoken, //TODO figure out how to drop !! here
                        region = effect.subscribeExtendedState.cursor.region
                    ) { r, s ->
                        eventQueue.put(
                            if (!s.error) {
                                ReceivingResult.ReceivingSucceeded(r!!)
                            } else {
                                ReceivingResult.ReceivingFailed(s)
                            }
                        )

                    }
                }, cancelFn = { silentCancel() })
            }
        }
    }
}

internal class RetryEffectExecutor(
    private val effectQueue: LinkedBlockingQueue<SubscribeEffectInvocation>,
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(3),
    private val retryPolicy: RetryPolicy = NoPolicy
) : EffectHandlerFactory<ScheduleRetry> {
    override fun handler(effect: ScheduleRetry): EffectHandler {
        executor.schedule(Callable {
            effectQueue.put(effect.retryableEffect)
        }, retryPolicy.computeDelay(effect.retryCount).seconds, TimeUnit.SECONDS).let {
            {
                it.cancel(true)
            }
        }
        return EffectHandler.create()
    }
}

internal interface IncomingPayloadProcessor {
    fun processIncomingPayload(message: SubscribeMessage)
}

internal class NewMessagesEffectExecutor(private val processor: IncomingPayloadProcessor) :
    EffectHandlerFactory<NewMessages> {
    override fun handler(effect: NewMessages): EffectHandler {
        effect.messages.forEach {
            processor.processIncomingPayload(it)
        }
        return EffectHandler.create()
    }
}
