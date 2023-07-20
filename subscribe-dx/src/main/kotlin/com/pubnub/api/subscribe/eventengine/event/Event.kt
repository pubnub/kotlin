package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.core.CoreException
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
    data class HandshakeFailure(val reason: CoreException) : Event()
    data class HandshakeReconnectSuccess(val subscriptionCursor: SubscriptionCursor) : Event()
    data class HandshakeReconnectFailure(val reason: CoreException) : Event()
    object HandshakeReconnectRetry : Event()
    data class HandshakeReconnectGiveup(val reason: CoreException) : Event()

    data class ReceiveSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    data class ReceiveFailure(val reason: CoreException) : Event()
    data class ReceiveReconnectSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    data class ReceiveReconnectFailure(val reason: CoreException) : Event()
    object ReceiveReconnectRetry : Event()
    data class ReceiveReconnectGiveup(val reason: CoreException) : Event()
    object UnsubscribeAll : Event()
}
