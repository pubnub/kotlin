package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.state.Event

interface SubscribeEvent : Event

object InitialEvent : SubscribeEvent

data class SubscriptionChanged(
    val channels: Set<String>,
    val groups: Set<String> = setOf()
) : SubscribeEvent

object Disconnect : SubscribeEvent
object Reconnect : SubscribeEvent
object Restore : SubscribeEvent
data class HandshakingSuccess(val cursor: Cursor) : SubscribeEvent
data class HandshakingFailure(val status: PNStatus) : SubscribeEvent
data class HandshakingReconnectingSuccess(val cursor: Cursor) : SubscribeEvent
object HandshakingReconnectingGiveUp : SubscribeEvent
data class HandshakingReconnectingFailure(val status: PNStatus) : SubscribeEvent
object HandshakingReconnectingRetry : SubscribeEvent
data class ReceivingSuccess(val subscribeEnvelope: SubscribeEnvelope) : SubscribeEvent
data class ReceivingFailure(val status: PNStatus) : SubscribeEvent
data class ReceiveReconnectingSuccess(val subscribeEnvelope: SubscribeEnvelope) : SubscribeEvent
object ReceiveReconnectingGiveUp : SubscribeEvent
data class ReceiveReconnectingFailure(val status: PNStatus) : SubscribeEvent
object ReceiveReconnectingRetry : SubscribeEvent
