package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.Event
import com.pubnub.api.models.consumer.pubsub.PNEvent

sealed class SubscribeEvent : Event {
    data class SubscriptionChanged(val channels: Set<String>, val channelGroups: Set<String>) : SubscribeEvent()
    data class SubscriptionRestored(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : SubscribeEvent()

    object Disconnect : SubscribeEvent()
    object Reconnect : SubscribeEvent()
    object UnsubscribeAll : SubscribeEvent()

    data class HandshakeSuccess(val subscriptionCursor: SubscriptionCursor) : SubscribeEvent()
    data class HandshakeFailure(val reason: PubNubException) : SubscribeEvent()
    data class HandshakeReconnectSuccess(val subscriptionCursor: SubscriptionCursor) : SubscribeEvent()
    data class HandshakeReconnectFailure(val reason: PubNubException) : SubscribeEvent()
    object HandshakeReconnectRetry : SubscribeEvent()
    data class HandshakeReconnectGiveup(val reason: PubNubException) : SubscribeEvent()

    data class ReceiveSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) :
        SubscribeEvent()

    data class ReceiveFailure(val reason: PubNubException) : SubscribeEvent()
    data class ReceiveReconnectSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) :
        SubscribeEvent()

    data class ReceiveReconnectFailure(val reason: PubNubException) : SubscribeEvent()
    object ReceiveReconnectRetry : SubscribeEvent()
    data class ReceiveReconnectGiveup(val reason: PubNubException) : SubscribeEvent()
}
