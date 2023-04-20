package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.CancelEffectInvocation
import com.pubnub.api.eventengine.ManagedEffectInvocation
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.eventengine.EffectInvocation as CoreEffectInvocation

sealed class EffectInvocation : CoreEffectInvocation {

    override val id: String = ReceiveReconnect::class.java.simpleName

    data class ReceiveMessages(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : EffectInvocation(),
        ManagedEffectInvocation

    object CancelReceiveMessages : EffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = ReceiveMessages::class.java.simpleName
    }

    data class ReceiveReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?
    ) : EffectInvocation(),
        ManagedEffectInvocation

    object CancelReceiveReconnect : EffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = ReceiveReconnect::class.java.simpleName
    }

    data class Handshake(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : EffectInvocation(),
        ManagedEffectInvocation

    object CancelHandshake : EffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = Handshake::class.java.simpleName
    }

    data class HandshakeReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : EffectInvocation(),
        ManagedEffectInvocation

    object CancelHandshakeReconnect : EffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = HandshakeReconnect::class.java.simpleName
    }

    data class EmitStatus(val status: PNStatusCategory) : EffectInvocation()
    data class EmitMessages(val messages: List<PNEvent>) : EffectInvocation()
}
