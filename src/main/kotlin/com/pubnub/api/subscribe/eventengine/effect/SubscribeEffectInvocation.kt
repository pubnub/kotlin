package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

interface EffectInvocation {
    val id: String
}

interface CancelEffectInvocation {
    val idToCancel: String
}

interface ManagedEffectInvocation

sealed class SubscribeEffectInvocation : EffectInvocation {

    override val id: String = ReceiveReconnect::class.java.simpleName

    data class ReceiveMessages(
        private val channels: List<String>,
        private val channelGroups: List<String>,
        private val subscriptionCursor: SubscriptionCursor
    ) : SubscribeEffectInvocation(),
        ManagedEffectInvocation

    object CancelReceiveMessages : SubscribeEffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = ReceiveMessages::class.java.simpleName
    }

    data class ReceiveReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeEffectInvocation(),
        ManagedEffectInvocation

    object CancelReceiveReconnect : SubscribeEffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = ReceiveReconnect::class.java.simpleName
    }

    data class Handshake(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : SubscribeEffectInvocation(),
        ManagedEffectInvocation

    object CancelHandshake : SubscribeEffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = Handshake::class.java.simpleName
    }
    data class HandshakeReconnect(
        val channels: List<String>,
        val channelGroups: List<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeEffectInvocation(),
        ManagedEffectInvocation

    object CancelHandshakeReconnect : SubscribeEffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = HandshakeReconnect::class.java.simpleName
    }

    data class EmitStatus(val status: PNStatusCategory) : SubscribeEffectInvocation()
    data class EmitMessages(val messages: List<PNEvent>) : SubscribeEffectInvocation()
}
