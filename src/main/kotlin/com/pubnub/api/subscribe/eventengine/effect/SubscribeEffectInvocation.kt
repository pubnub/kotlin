package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.Cancel
import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.EffectInvocationType
import com.pubnub.api.eventengine.Managed
import com.pubnub.api.eventengine.NonManaged
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

sealed class SubscribeEffectInvocation(override val type: EffectInvocationType) : EffectInvocation {

    override val id: String = ReceiveReconnect::class.java.simpleName

    data class ReceiveMessages(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : SubscribeEffectInvocation(Managed)

    object CancelReceiveMessages :
        SubscribeEffectInvocation(Cancel(idToCancel = ReceiveMessages::class.java.simpleName))

    data class ReceiveReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeEffectInvocation(Managed)

    object CancelReceiveReconnect :
        SubscribeEffectInvocation(Cancel(ReceiveReconnect::class.java.simpleName))

    data class Handshake(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : SubscribeEffectInvocation(Managed)

    object CancelHandshake :
        SubscribeEffectInvocation(Cancel(idToCancel = Handshake::class.java.simpleName))

    data class HandshakeReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeEffectInvocation(Managed)

    object CancelHandshakeReconnect :
        SubscribeEffectInvocation(Cancel(idToCancel = HandshakeReconnect::class.java.simpleName))

    data class EmitStatus(val status: PNStatusCategory) : SubscribeEffectInvocation(NonManaged)
    data class EmitMessages(val messages: List<PNEvent>) : SubscribeEffectInvocation(NonManaged)
}
