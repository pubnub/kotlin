package com.pubnub.api.subscribe.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.state.EffectEngine
import com.pubnub.api.state.QueuedEventEngine
import com.pubnub.api.subscribe.NewSubscribeModule
import java.util.concurrent.LinkedBlockingQueue

internal class InternalSubscribeModule(
    private val eventEngine: QueuedEventEngine<SubscribeState, SubscribeEvent, SubscribeEffect>,
    private val effectEngine: EffectEngine<SubscribeEffect>
) : NewSubscribeModule {

    companion object {
        private fun createRetryPolicy(configuration: PNConfiguration) = when (configuration.reconnectionPolicy) {
            PNReconnectionPolicy.NONE -> NoPolicy
            PNReconnectionPolicy.LINEAR -> LinearPolicy(configuration.maximumReconnectionRetries)
            PNReconnectionPolicy.EXPONENTIAL -> ExponentialPolicy(configuration.maximumReconnectionRetries)
        }

        fun create(
            pubnub: PubNub,
            incomingPayloadProcessor: IncomingPayloadProcessor,
            listenerManager: ListenerManager
        ): NewSubscribeModule {
            val eventQueue = LinkedBlockingQueue<SubscribeEvent>(100)
            val effectQueue = LinkedBlockingQueue<SubscribeEffect>(100)
            val retryPolicy: RetryPolicy = createRetryPolicy(pubnub.configuration)
            val eventEngine = queuedSubscribeEventEngine(
                eventQueue = eventQueue,
                effectQueue = effectQueue,
                shouldRetry = retryPolicy::shouldRetry
            )
            val effectEngine = SubscribeEffectEngine.create(
                pubnub = pubnub,
                eventQueue = eventQueue,
                effectQueue = effectQueue,
                retryPolicy = retryPolicy,
                incomingPayloadProcessor = incomingPayloadProcessor,
                listenerManager = listenerManager
            )

            return InternalSubscribeModule(
                eventEngine = eventEngine,
                effectEngine = effectEngine
            )
        }
    }

    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        eventEngine.handle(
            Commands.SubscribeIssued(
                channels = channels,
                groups = channelGroups
            )
        )
    }

    override fun unsubscribe(
        channels: List<String>,
        channelGroups: List<String>
    ) {
        eventEngine.handle(
            Commands.UnsubscribeIssued(
                channels = channels,
                groups = channelGroups
            )
        )
    }

    override fun unsubscribeAll() {
        eventEngine.handle(
            Commands.UnsubscribeAllIssued
        )
    }

    override fun getSubscribedChannels(): List<String> = eventEngine.state().status.channels.toList()

    override fun getSubscribedChannelGroups(): List<String> = eventEngine.state().status.groups.toList()

    fun status(): SubscriptionStatus {
        return eventEngine.state().status
    }

    override fun cancel() {
        eventEngine.cancel()
        effectEngine.cancel()
    }
}
