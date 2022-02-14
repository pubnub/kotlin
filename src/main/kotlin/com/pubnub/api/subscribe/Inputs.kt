package com.pubnub.api.subscribe

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.state.Input
import com.pubnub.api.subscribe.internal.Cursor

sealed interface SubscribeInput : Input

sealed class SubscribeCommands : SubscribeInput {
    data class Subscribe(
        val channels: List<String>,
        val groups: List<String> = listOf(),
        val cursor: Cursor? = null
    ) : SubscribeCommands()

    data class Unsubscribe(
        val channels: List<String>,
        val groups: List<String> = listOf()
    ) : SubscribeCommands()

    object UnsubscribeAll : SubscribeCommands()
}

sealed class HandshakeResult : SubscribeInput {
    data class HandshakeSuccess(val cursor: Cursor) : HandshakeResult()
    object HandshakeFail : HandshakeResult()
}

sealed class ReceivingResult : SubscribeInput {
    data class ReceivingSuccess(val subscribeEnvelope: SubscribeEnvelope) : ReceivingResult()
    object ReceivingFail : ReceivingResult()
}


