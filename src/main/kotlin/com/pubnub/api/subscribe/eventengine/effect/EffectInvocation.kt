package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class EffectInvocation {

    data class ReceiveMessages(
        private val channels: List<String>,
        private val channelGroups: List<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : EffectInvocation()

    object CancelReceiveMessages : EffectInvocation()

    data class ReceiveReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?
    ) : EffectInvocation()

    object CancelReceiveReconnect : EffectInvocation()

    data class Handshake(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : EffectInvocation()

    object CancelHandshake : EffectInvocation()
    data class HandshakeReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : EffectInvocation()

    object CancelHandshakeReconnect : EffectInvocation()
    data class EmitStatus(val status: PNStatusCategory) : EffectInvocation()
    data class EmitMessages(val messages: List<PNEvent>) : EffectInvocation()
}
