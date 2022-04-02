package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.EffectInvocation

sealed class SubscribeEffectInvocation : EffectInvocation

data class EmitEvents(val messages: List<SubscribeMessage>) : SubscribeEffectInvocation()

sealed class SubscribeHttpEffectInvocation : SubscribeEffectInvocation()

data class ReceiveEvents(
    val subscribeExtendedState: SubscribeExtendedState
) : SubscribeHttpEffectInvocation()

data class Handshake(
    val subscribeExtendedState: SubscribeExtendedState
) : SubscribeHttpEffectInvocation()

data class ReceiveEventsReconnect(
    val subscribeExtendedState: SubscribeExtendedState,
    val attempt: Int
) : SubscribeEffectInvocation()

data class HandshakeReconnect(
    val subscribeExtendedState: SubscribeExtendedState,
    val attempt: Int
) : SubscribeEffectInvocation()

data class CancelEffectInvocation(val idToCancel: String) : SubscribeEffectInvocation()

sealed class NotificationEffect : SubscribeEffectInvocation()

object Connected : NotificationEffect()

data class Disconnected(val reason: String = "reason") : NotificationEffect()

object Reconnected : NotificationEffect()