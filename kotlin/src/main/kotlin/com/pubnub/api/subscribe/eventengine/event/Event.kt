package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.eventengine.Event as CoreEvent

sealed class Event : CoreEvent {
    data class SubscriptionChanged(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : Event()

    data class SubscriptionRestored(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event()

    object Disconnect : Event()
    object Reconnect : Event()

    data class HandshakeSuccess(val subscriptionCursor: SubscriptionCursor) : Event()
    data class HandshakeFailure(val reason: PubNubException) : Event()
    data class HandshakeReconnectSuccess(val subscriptionCursor: SubscriptionCursor) : Event()
    data class HandshakeReconnectFailure(val reason: PubNubException) : Event()
    object HandshakeReconnectRetry : Event()
    data class HandshakeReconnectGiveup(val reason: PubNubException) : Event()

    data class ReceiveSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    data class ReceiveFailure(val reason: PubNubException) : Event()
    data class ReceiveReconnectSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    data class ReceiveReconnectFailure(val reason: PubNubException) : Event()
    object ReceiveReconnectRetry : Event()
    data class ReceiveReconnectGiveup(val reason: PubNubException) : Event()
    object UnsubscribeAll : Event()
}
