package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class EffectInvocation {

    data class ReceiveMessagesRequest(   //rename to RECEIVE_EVENTS or what will be agreed e.g. ReceiveMessages
        private val channels: List<String>,
        private val channelGroups: List<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : EffectInvocation()
    data class ReceiveReconnect(private val channels: List<String>, val channelGroups: List<String>) : EffectInvocation()
    object CancelReceiveReconnect: EffectInvocation()
    object CancelReceiveEvents: EffectInvocation() //expecting rename to CancelReceiveMessages

    object CancelPendingReceiveMessagesRequest : EffectInvocation()
    data class HandshakeRequest(val channels: List<String>, val channelGroups: List<String>) : EffectInvocation()
    object CancelPendingHandshakeRequest : EffectInvocation()
    data class HandshakeReconnect(val channels: List<String>, val channelGroups: List<String>) : EffectInvocation()
    object CancelHandshakeReconnect : EffectInvocation()
    object START_RECONNECTION_DELAY_TIMER : EffectInvocation()
    object CANCEL_START_RECONNECTION_DELAY_TIMER : EffectInvocation()
    object DELIVER_MESSAGE_REQUEST : EffectInvocation() //emit messages
    data class EmitStatus(val status: String) : EffectInvocation() //toDo change String to something else
    data class EmitMessages(val pnEvents: List<PNEvent>) : EffectInvocation()  //Deliver messages requested
}