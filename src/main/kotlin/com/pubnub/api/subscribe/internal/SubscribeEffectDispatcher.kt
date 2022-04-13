package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.EffectDispatcher
import com.pubnub.api.state.EffectHandler
import com.pubnub.api.state.EffectHandlerFactory
import com.pubnub.api.state.EffectTracker
import com.pubnub.api.state.ManagedEffectHandler
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

internal class SubscribeEffectDispatcher(
    private val httpHandler: EffectHandlerFactory<SubscribeHttpEffectInvocation>,
    private val handshakeReconnectHandlerFactory: EffectHandlerFactory<HandshakeReconnect>,
    private val receiveEventsReconnectHandlerFactory: EffectHandlerFactory<ReceiveEventsReconnect>,
    private val emitEventsEffectExecutor: EffectHandlerFactory<EmitEvents>,
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
            is EmitEvents -> emitEventsEffectExecutor.handler(effect)
            is NotificationEffect -> notificationEffectExecutor.handler(effect)
            is HandshakeReconnect -> handshakeReconnectHandlerFactory.handler(effect)
            is ReceiveEventsReconnect -> receiveEventsReconnectHandlerFactory.handler(effect)
        }
        handler?.start()
        if (handler is ManagedEffectHandler) {
            startTracking(effect.id(), handler)
        }
    }

    fun cancel() {
    }
}

internal class ReceiveEventsReconnectHandlerFactory(
    private val pubnub: PubNub,
    private val retryPolicy: RetryPolicy,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>,
    private val executor: ScheduledExecutorService
) : EffectHandlerFactory<ReceiveEventsReconnect> {

    override fun handler(effect: ReceiveEventsReconnect): EffectHandler {
        return EffectHandler.create(
            startFn = {

                if (!retryPolicy.shouldRetry(effect.subscribeExtendedState.attempt)) {
                    eventQueue.put(
                        ReceiveReconnectingGiveUp
                    )
                }

                val remoteAction = pubnub.receiveEvents(
                    channels = effect.subscribeExtendedState.channels.toList(),
                    channelGroups = effect.subscribeExtendedState.groups.toList(),
                    timetoken = effect.subscribeExtendedState.cursor?.timetoken!!,
                    region = effect.subscribeExtendedState.cursor.region
                )

                val delay = retryPolicy.computeDelay(effect.subscribeExtendedState.attempt)
                val scheduled = executor.schedule(
                    {
                        remoteAction.async { r, s ->
                            eventQueue.put(
                                if (!s.error) {
                                    ReceiveReconnectingSuccess(
                                        r!!
                                    )
                                } else {
                                    ReceiveReconnectingFailure(s)
                                }
                            )
                        }
                    },
                    delay.toMillis(), TimeUnit.MILLISECONDS
                )
                scheduled to remoteAction
            },
            cancelFn = {
                second.silentCancel()
                first.cancel(true)
            }
        )
    }
}

internal class HandshakeReconnectHandlerFactory(
    private val pubnub: PubNub,
    private val retryPolicy: RetryPolicy,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>,
    private val executor: ScheduledExecutorService
) : EffectHandlerFactory<HandshakeReconnect> {
    override fun handler(effect: HandshakeReconnect): EffectHandler {
        return EffectHandler.create(
            startFn = {
                if (!retryPolicy.shouldRetry(effect.subscribeExtendedState.attempt)) {
                    eventQueue.put(
                        ReceiveReconnectingGiveUp
                    )
                }

                val remoteAction = pubnub.handshake(
                    channels = effect.subscribeExtendedState.channels.toList(),
                    channelGroups = effect.subscribeExtendedState.groups.toList()
                )

                val delay = retryPolicy.computeDelay(effect.subscribeExtendedState.attempt)
                val scheduled = executor.schedule(
                    {
                        remoteAction.async { r, s ->
                            eventQueue.put(
                                if (!s.error) {
                                    HandshakingSuccess(
                                        Cursor(
                                            timetoken = r!!.metadata.timetoken, // TODO we could improve callback to avoid !! here
                                            region = r.metadata.region
                                        )
                                    )
                                } else {
                                    HandshakingFailure(s)
                                }
                            )
                        }
                    },
                    delay.toMillis(), TimeUnit.MILLISECONDS
                )
                scheduled to remoteAction
            },
            cancelFn = {
                second.silentCancel()
                first.cancel(true)
            }
        )
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
    private val pubnub: PubNub,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>
) : EffectHandlerFactory<SubscribeHttpEffectInvocation> {

    override fun handler(effect: SubscribeHttpEffectInvocation): EffectHandler {
        return when (effect) {
            is Handshake -> {
                EffectHandler.create(
                    startFn = {
                        val remoteAction =
                            pubnub.handshake(
                                channels = effect.subscribeExtendedState.channels.toList(),
                                channelGroups = effect.subscribeExtendedState.groups.toList()
                            )
                        remoteAction.async { r, s ->
                            eventQueue.put(
                                if (!s.error) {
                                    HandshakingSuccess(
                                        Cursor(
                                            timetoken = r!!.metadata.timetoken, // TODO we could improve callback to avoid !! here
                                            region = r.metadata.region
                                        )
                                    )
                                } else {
                                    HandshakingFailure(s)
                                }
                            )
                        }
                        remoteAction
                    },
                    cancelFn = { silentCancel() }
                )
            }
            is ReceiveEvents -> {

                EffectHandler.create(
                    startFn = {
                        val remoteAction = pubnub.receiveEvents(
                            channels = effect.subscribeExtendedState.channels.toList(),
                            channelGroups = effect.subscribeExtendedState.groups.toList(),
                            timetoken = effect.subscribeExtendedState.cursor!!.timetoken, // TODO figure out how to drop !! here
                            region = effect.subscribeExtendedState.cursor.region
                        )
                        remoteAction.async { r, s ->
                            eventQueue.put(
                                if (!s.error) {
                                    ReceivingSuccess(r!!)
                                } else {
                                    ReceivingFailure(s)
                                }
                            )
                        }
                        remoteAction
                    },
                    cancelFn = { silentCancel() }
                )
            }
        }
    }
}

internal interface IncomingPayloadProcessor {
    fun processIncomingPayload(message: SubscribeMessage)
}

internal class NewMessagesEffectExecutor(private val processor: IncomingPayloadProcessor) :
    EffectHandlerFactory<EmitEvents> {
    override fun handler(effect: EmitEvents): EffectHandler {
        effect.messages.forEach {
            processor.processIncomingPayload(it)
        }
        return EffectHandler.create()
    }
}
