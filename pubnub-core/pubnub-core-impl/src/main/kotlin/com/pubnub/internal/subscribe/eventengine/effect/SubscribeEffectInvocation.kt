package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.eventengine.Cancel
import com.pubnub.internal.eventengine.EffectInvocation
import com.pubnub.internal.eventengine.EffectInvocationType
import com.pubnub.internal.eventengine.Managed
import com.pubnub.internal.eventengine.NonManaged
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor

internal sealed class SubscribeEffectInvocation(override val type: EffectInvocationType) : EffectInvocation {
    override val id: String = "any value for NonManged and Cancel effect"

    data class ReceiveMessages(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor,
    ) : SubscribeEffectInvocation(Managed) {
        override val id: String = ReceiveMessages::class.java.simpleName
    }

    object CancelReceiveMessages :
        SubscribeEffectInvocation(Cancel(idToCancel = ReceiveMessages::class.java.simpleName))

    data class ReceiveReconnect(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?,
    ) : SubscribeEffectInvocation(Managed) {
        override val id: String = ReceiveReconnect::class.java.simpleName
    }

    object CancelReceiveReconnect :
        SubscribeEffectInvocation(Cancel(ReceiveReconnect::class.java.simpleName))

    data class Handshake(
        val channels: Set<String>,
        val channelGroups: Set<String>,
    ) : SubscribeEffectInvocation(Managed) {
        override val id: String = Handshake::class.java.simpleName
    }

    object CancelHandshake :
        SubscribeEffectInvocation(Cancel(idToCancel = Handshake::class.java.simpleName))

    data class HandshakeReconnect(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val attempts: Int,
        val reason: PubNubException?,
    ) : SubscribeEffectInvocation(Managed) {
        override val id: String = HandshakeReconnect::class.java.simpleName
    }

    object CancelHandshakeReconnect :
        SubscribeEffectInvocation(Cancel(idToCancel = HandshakeReconnect::class.java.simpleName))

    data class EmitStatus(val status: PNStatus) : SubscribeEffectInvocation(NonManaged)

    data class EmitMessages(val messages: List<PNEvent>) : SubscribeEffectInvocation(NonManaged)
}
