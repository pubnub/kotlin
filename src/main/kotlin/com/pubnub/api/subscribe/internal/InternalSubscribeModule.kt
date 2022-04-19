package com.pubnub.api.subscribe.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.state.EffectDispatcher
import com.pubnub.api.state.internal.IntModule
import com.pubnub.api.subscribe.NewSubscribeModule
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService

internal class InternalSubscribeModule(
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent>,
    val moduleInternals: IntModule<SubscribeState, SubscribeEvent, SubscribeEffectInvocation>
) : NewSubscribeModule {

    companion object {
        private fun createRetryPolicy(configuration: PNConfiguration) = when (configuration.reconnectionPolicy) {
            PNReconnectionPolicy.NONE -> NoPolicy
            PNReconnectionPolicy.LINEAR -> LinearPolicy(configuration.maximumReconnectionRetries)
            PNReconnectionPolicy.EXPONENTIAL -> ExponentialPolicy(configuration.maximumReconnectionRetries)
        }

        fun create(
            pubnub: PubNub,
            listenerManager: ListenerManager,
            incomingPayloadProcessor: IncomingPayloadProcessor,
            scheduleExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(2),
            retryPolicy: RetryPolicy = createRetryPolicy(pubnub.configuration),
            engineAndEffects: Pair<SubscribeEventEngine, List<SubscribeEffectInvocation>> = subscribeEventEngine(),
            eventQueue: LinkedBlockingQueue<SubscribeEvent> = LinkedBlockingQueue(100),
            effectQueue: LinkedBlockingQueue<SubscribeEffectInvocation> =
                LinkedBlockingQueue<SubscribeEffectInvocation>(
                    100
                ).apply { engineAndEffects.second.forEach(::put) },
            effectDispatcher: EffectDispatcher<SubscribeEffectInvocation> = SubscribeEffectDispatcher(
                httpHandler = HttpCallExecutor(
                    pubnub = pubnub, eventQueue = eventQueue
                ),
                handshakeReconnectHandlerFactory = HandshakeReconnectHandlerFactory(
                    pubnub = pubnub,
                    eventQueue = eventQueue,
                    executor = scheduleExecutorService,
                    retryPolicy = retryPolicy
                ),
                receiveEventsReconnectHandlerFactory = ReceiveEventsReconnectHandlerFactory(
                    pubnub = pubnub,
                    eventQueue = eventQueue,
                    executor = scheduleExecutorService,
                    retryPolicy = retryPolicy
                ),
                emitEventsEffectExecutor = NewMessagesEffectExecutor(incomingPayloadProcessor),
                notificationEffectExecutor = NotificationExecutor(listenerManager)
            )
        ): NewSubscribeModule {

            val moduleInternals = IntModule(
                engine = engineAndEffects.first,
                eventQueue = eventQueue,
                effectDispatcher = effectDispatcher,
                effectQueue = effectQueue
            )
            return InternalSubscribeModule(eventQueue, moduleInternals = moduleInternals)
        }
    }

    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        val extendedSubscribeState = moduleInternals.currentState().extendedState
        val newChannels = extendedSubscribeState.channels + channels.toSet()
        val newGroups = extendedSubscribeState.groups + channelGroups.toSet()

        if (newChannels == extendedSubscribeState.channels && newGroups == extendedSubscribeState.groups) {
            return
        }

        eventQueue.put(
            SubscriptionChanged(
                channels = newChannels, groups = newGroups
            )
        )
    }

    override fun unsubscribe(
        channels: List<String>,
        channelGroups: List<String>
    ) {
        val extendedSubscribeState = moduleInternals.currentState().extendedState
        val newChannels = extendedSubscribeState.channels - channels.toSet()
        val newGroups = extendedSubscribeState.groups - channelGroups.toSet()

        if (newChannels == extendedSubscribeState.channels && newGroups == extendedSubscribeState.groups) {
            return
        }

        if (newChannels.isEmpty() && newGroups.isEmpty()) {
            eventQueue.put(
                Disconnect
            )
        } else {
            eventQueue.put(
                SubscriptionChanged(
                    channels = extendedSubscribeState.channels + channels,
                    groups = extendedSubscribeState.groups + channelGroups
                )
            )
        }
    }

    override fun unsubscribeAll() {
        eventQueue.put(
            Disconnect
        )
    }

    override fun getSubscribedChannels(): List<String> = status().channels.toList()

    override fun getSubscribedChannelGroups(): List<String> = status().groups.toList()

    fun status(): SubscribeExtendedState {
        return moduleInternals.currentState().extendedState
    }

    override fun cancel() {
        moduleInternals.cancel()
    }
}
