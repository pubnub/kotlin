package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.EffectInvocation

sealed class SubscribeEffectInvocation : EffectInvocation

data class NewMessages(val messages: List<SubscribeMessage>) : SubscribeEffectInvocation()

sealed class SubscribeHttpEffectInvocation : SubscribeEffectInvocation() {
    data class ReceiveMessagesHttpCallEffectInvocation(
        val subscribeExtendedState: SubscribeExtendedState
    ) : SubscribeHttpEffectInvocation()

    data class HandshakeHttpCallEffectInvocation(
        val subscribeExtendedState: SubscribeExtendedState
    ) : SubscribeHttpEffectInvocation()
}

data class CancelEffectInvocation(val idToCancel: String) : SubscribeEffectInvocation()

data class ScheduleRetry(val retryableEffect: SubscribeHttpEffectInvocation, val retryCount: Int) :
    SubscribeEffectInvocation()


sealed class NotificationEffect : SubscribeEffectInvocation()

object Connected : NotificationEffect()

data class Disconnected(val reason: String = "reason") : NotificationEffect()

object Reconnected : NotificationEffect()