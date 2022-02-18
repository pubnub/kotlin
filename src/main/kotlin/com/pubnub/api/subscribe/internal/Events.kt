package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.state.Event

sealed interface SubscribeEvent : Event

object InitialEvent : SubscribeEvent

sealed class Commands : SubscribeEvent {
    data class SubscribeIssued(
        val channels: List<String>,
        val groups: List<String> = listOf(),
        val cursor: Cursor? = null
    ) : Commands()

    data class UnsubscribeIssued(
        val channels: List<String>,
        val groups: List<String> = listOf()
    ) : Commands()

    object UnsubscribeAllIssued : Commands()
}

sealed class HandshakeResult : SubscribeEvent {
    data class HandshakeSucceeded(val cursor: Cursor) : HandshakeResult()
    object HandshakeFailed : HandshakeResult()
}

sealed class ReceivingResult : SubscribeEvent {
    data class ReceivingSucceeded(val subscribeEnvelope: SubscribeEnvelope) : ReceivingResult()
    object ReceivingFailed : ReceivingResult()
}


