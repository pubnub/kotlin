package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class EffectInvocation {

    data class ReceiveMessages(
        private val channels: List<String>,
        private val channelGroups: List<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : EffectInvocation()

    data class ReceiveReconnect(private val channels: List<String>, val channelGroups: List<String>) :
        EffectInvocation() // todo add val attempts: Int

    object CancelReceiveReconnect : EffectInvocation()
    object CancelReceiveMessages : EffectInvocation()
    data class Handshake(val channels: List<String>, val channelGroups: List<String>) : EffectInvocation()
    object CancelHandshake : EffectInvocation()
    data class HandshakeReconnect(val channels: List<String>, val channelGroups: List<String>) : EffectInvocation()
    object CancelHandshakeReconnect : EffectInvocation()
    data class EmitStatus(val status: String) : EffectInvocation() // toDo change String to something else
    data class EmitMessages(val messages: List<PNEvent>) : EffectInvocation() // Deliver messages requested
}
