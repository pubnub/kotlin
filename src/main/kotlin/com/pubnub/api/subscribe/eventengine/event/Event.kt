package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.pubsub.PNEvent

sealed class Event {
    class SubscriptionChanged(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event()

    object Disconnect : Event()
    object Reconnect : Event()

    data class HandshakeSuccess(val subscriptionCursor: SubscriptionCursor) : Event() // zamienić na SubscriptionCursor
    data class SubscriptionRestored(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event()

    data class HandshakeFailure(val reason: PubNubException) : Event()
    data class HandshakeReconnectGiveUp(val reason: PubNubException) : Event()
    data class HandshakeReconnectSuccess(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event()

    data class HandshakeReconnectFailure(val reason: PubNubException) : Event()
    object HandshakeReconnectRetry : Event()
    data class ReceiveSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    data class ReceiveFailure(val reason: PubNubException) : Event()

    class ReceiveReconnectFailure(
        val reason: PubNubException
    ) : Event()

    class ReceiveReconnectGiveUp(val reason: PubNubException) : Event()
    class ReceiveReconnectSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    class ReceiveReconnectRetry : Event()
    class Fail : Event()
    class Success : Event()
}
