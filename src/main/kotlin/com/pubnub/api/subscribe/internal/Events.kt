package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.state.Event

sealed interface SubscribeEvent : Event

object InitialEvent : SubscribeEvent

data class SubscriptionChanged(
    val channels: List<String>,
    val groups: List<String> = listOf()
) : SubscribeEvent

object Disconnect : SubscribeEvent
object Reconnect : SubscribeEvent
object Restore : SubscribeEvent
data class HandshakingSuccess(val cursor: Cursor) : SubscribeEvent
data class HandshakingFailure(val status: PNStatus) : SubscribeEvent
data class HandshakingReconnectingSuccess(val cursor: Cursor) : SubscribeEvent
object HandshakingReconnectingGiveUp : SubscribeEvent
object HandshakingReconnectingFailure : SubscribeEvent
object HandshakingReconnectingRetry : SubscribeEvent
data class ReceivingSuccess(val subscribeEnvelope: SubscribeEnvelope) : SubscribeEvent
data class ReceivingFailure(val status: PNStatus) : SubscribeEvent
data class ReconnectingSuccess(val subscribeEnvelope: SubscribeEnvelope) : SubscribeEvent
object ReconnectingGiveUp : SubscribeEvent
data class ReconnectingFailure(val status: PNStatus) : SubscribeEvent
object ReconnectingRetry : SubscribeEvent
