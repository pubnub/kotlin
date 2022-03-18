package com.pubnub.api.subscribe.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.state.internal.IntModule
import com.pubnub.api.subscribe.NewSubscribeModule
import java.util.concurrent.LinkedBlockingQueue


internal class InternalSubscribeModule(
    pubnub: PubNub,
    engineAndEffects: Pair<SubscribeEventEngine, List<SubscribeEffectInvocation>> = subscribeEventEngine(
        createRetryPolicy(pubnub.configuration)::shouldRetry
    ),
    effectDispatcher: SubscribeEffectDispatcher,
    private val eventQueue: LinkedBlockingQueue<SubscribeEvent> = LinkedBlockingQueue(100),
    private val effectQueue: LinkedBlockingQueue<SubscribeEffectInvocation> = LinkedBlockingQueue<SubscribeEffectInvocation>(
        100
    ).apply { engineAndEffects.second.forEach(::put) },
    val moduleInternals: IntModule<SubscribeState, SubscribeEvent, SubscribeEffectInvocation> = IntModule(
        engine = engineAndEffects.first,
        eventQueue = eventQueue,
        effectDispatcher = effectDispatcher,
        effectQueue = effectQueue
    )
) : NewSubscribeModule {

    companion object {
        private fun createRetryPolicy(configuration: PNConfiguration) = when (configuration.reconnectionPolicy) {
            PNReconnectionPolicy.NONE -> NoPolicy
            PNReconnectionPolicy.LINEAR -> LinearPolicy(configuration.maximumReconnectionRetries)
            PNReconnectionPolicy.EXPONENTIAL -> ExponentialPolicy(configuration.maximumReconnectionRetries)
        }
    }

    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        eventQueue.put(
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
        eventQueue.put(
            Commands.UnsubscribeIssued(
                channels = channels,
                groups = channelGroups
            )
        )
    }

    override fun unsubscribeAll() {
        eventQueue.put(
            Commands.UnsubscribeAllIssued
        )
    }

    override fun getSubscribedChannels(): List<String> = status().channels.toList()

    override fun getSubscribedChannelGroups(): List<String> = status().groups.toList()

    fun status(): SubscriptionStatus {
        return moduleInternals.currentState().status
    }

    override fun cancel() {
        moduleInternals.cancel()
    }
}
