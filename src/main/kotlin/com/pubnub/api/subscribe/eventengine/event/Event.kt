package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.pubsub.PNEvent

sealed class Event {
    class SubscriptionChanged(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event() // todo withPresence omineliśmy

    class AllChannelUnsubscribe : Event()
    class Disconnect : Event()
    data class Reconnect(val channels: List<String>, val channelGroups: List<String>) : Event()

    class Restore : Event()
    class HandshakingDelayExpired : Event()
    data class HandshakeSuccess(val subscriptionCursor: SubscriptionCursor) : Event() // zamienić na SubscriptionCursor
    data class SubscriptionRestored(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event()

    class HandshakeFailure : Event()
    class HandshakeReconnectGiveUp : Event()
    data class HandshakeReconnectSuccess(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event()

    data class HandshakeReconnectFailure(val channels: List<String>, val channelGroups: List<String>) : Event()
    data class HandshakeReconnectRetry(val channels: List<String>, val channelGroups: List<String>) : Event()
    data class ReceiveSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    data class ReceiveFailure(val channels: List<String>, val channelGroups: List<String>, val reason: PubNubError) :
        Event() // todo czy potrzebne jest val channels: List<String>, val channelGroups: List<String> w dokumentacji nie ma, napisałem pytanie w PRze

    class ReceiveReconnectFailure(
        val channels: List<String>,
        val channelGroups: List<String>,
        val reason: PubNubError
    ) : Event() // todo czy potrzebne jest val channels: List<String>, val channelGroups: List<String> w dokumentacji nie ma

    class ReceiveReconnectGiveUp(val reason: PubNubError) : Event() // todo jakie będzie zastosowanie tego?
    class ReceiveReconnectSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    class ReceiveReconnectRetry() : Event()
    class Fail : Event()
    class Success : Event()
}
