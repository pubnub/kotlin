package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.pubsub.PNEvent

//sealed interface Event


sealed class Event {
    //common for all states
    class SubscriptionChanged(val channels: List<String>, val channelGroups: List<String>, val subscriptionCursor: SubscriptionCursor) :
        Event() //todo withPresence omineliśmy

    class AuthorizationChanged : Event()
    class AllChannelUnsubscribe : Event()
    class Disconnect : Event()
    data class Reconnect(val channels: List<String>, val channelGroups: List<String>) : Event()

    //UNSUBSCRIBED specific
    class Restore : Event()

    //HANDSHAKING specific
    class HandshakingDelayExpired : Event()
    data class HandshakeSuccess(val subscriptionCursor: SubscriptionCursor) : Event() //zamienić na SubscriptionCursor
    data class SubscriptionRestored(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : Event()

    class HandshakeFailure : Event()
    class HandshakeReconnectGiveUp : Event()
    data class HandshakeReconnectSuccess(val channels: List<String>, val channelGroups: List<String>, val subscriptionCursor: SubscriptionCursor) : Event()
    class ReconnectionLimitExceed : Event()
    data class HandshakeReconnectFailure(val channels: List<String>, val channelGroups: List<String>) : Event()
    data class HandshakeReconnectRetry(val channels: List<String>, val channelGroups: List<String>) :  Event()

    //HANDSHAKE_FAILED specific
    //noting

    //RECONNECTING specific
    data class ReceiveSuccess(val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor) : Event()
    data class ReceiveFailure(val channels: List<String>, val channelGroups: List<String>, val reason: PubNubError) : Event() //todo czy potrzebne jest val channels: List<String>, val channelGroups: List<String> w dokumentacji nie ma
    class ReconnectionDelayExpired : Event()  //te cztery podobne do tych z HANDSHAKING czy są takie same czy nie
    class Fail : Event()
    class Success : Event()
    class ReconnectionLimitExceeded : Event()
}


//
////events that will be supported when EE is in RECONNECTING state
//sealed class ReconnectingStateEvents : Event {
//    class ReconnectionDelayExpired : ReconnectingStateEvents()
//    class Fail : ReconnectingStateEvents()
//    class Success : ReconnectingStateEvents()
//    class ReconnectionLimitExceeded : ReconnectingStateEvents()
//
//}
//
//
////events that will be supported when EE is in RECEIVING state
//sealed class ReceivingStateEvents : Event {
//    class ReceiveMessageFail : ReceivingStateEvents()
//    class ReceiveMessageSuccess : ReceivingStateEvents()
//    class SubscriptionChange : ReceivingStateEvents()
//    class AuthorizationChanged : ReceivingStateEvents()
//    class AllChannelsUnsubscribed : ReceivingStateEvents()
//    class Disconnect : ReceivingStateEvents()
//    class Reconnect : ReceivingStateEvents()
//}


//transition events from Unsubscribed state

