package com.pubnub.internal.subscribe.eventengine.event

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.eventengine.Event

internal sealed class SubscribeEvent : Event {
    class SubscriptionChanged(channels: Set<String>, channelGroups: Set<String>) : SubscribeEvent() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()
    }
    class SubscriptionRestored(
        channels: Set<String>,
        channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : SubscribeEvent() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()
    }

    object Disconnect : SubscribeEvent()
    data class Reconnect(val subscriptionCursor: SubscriptionCursor? = null) : SubscribeEvent()
    object UnsubscribeAll : SubscribeEvent()

    data class HandshakeSuccess(val subscriptionCursor: SubscriptionCursor) : SubscribeEvent()
    data class HandshakeFailure(val reason: PubNubException) : SubscribeEvent()
    data class HandshakeReconnectSuccess(val subscriptionCursor: SubscriptionCursor) : SubscribeEvent()
    data class HandshakeReconnectFailure(val reason: PubNubException) : SubscribeEvent()
    data class HandshakeReconnectGiveup(val reason: PubNubException) : SubscribeEvent()

    data class ReceiveSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) :
        SubscribeEvent()

    data class ReceiveFailure(val reason: PubNubException) : SubscribeEvent()
    data class ReceiveReconnectSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) :
        SubscribeEvent()

    data class ReceiveReconnectFailure(val reason: PubNubException) : SubscribeEvent()
    data class ReceiveReconnectGiveup(val reason: PubNubException) : SubscribeEvent()
}
