package com.pubnub.api.subscribe

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.state.Event
import com.pubnub.api.subscribe.internal.Cursor

sealed interface SubscribeEvent : Event

sealed class Commands : SubscribeEvent {
    data class SubscribeCommandIssued(
        val channels: List<String>,
        val groups: List<String> = listOf(),
        val cursor: Cursor? = null
    ) : Commands()

    data class UnsubscribeCommandIssued(
        val channels: List<String>,
        val groups: List<String> = listOf()
    ) : Commands()

    object UnsubscribeAllCommandIssued : Commands()
}

sealed class HandshakeResult : SubscribeEvent {
    data class HandshakeSucceeded(val cursor: Cursor) : HandshakeResult()
    object HandshakeFailed : HandshakeResult()
}

sealed class ReceivingResult : SubscribeEvent {
    data class ReceivingSucceeded(val subscribeEnvelope: SubscribeEnvelope) : ReceivingResult()
    object ReceivingFailed : ReceivingResult()
}


