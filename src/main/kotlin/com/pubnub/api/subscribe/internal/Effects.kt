package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.EffectInvocation

sealed class SubscribeEffectInvocation : EffectInvocation

data class NewMessages(val messages: List<SubscribeMessage>) : SubscribeEffectInvocation()

data class NewState(val name: String, val status: SubscriptionStatus) : SubscribeEffectInvocation()

sealed class SubscribeHttpEffectInvocation : SubscribeEffectInvocation() {
    data class ReceiveMessagesHttpCallEffectInvocation(
        val subscriptionStatus: SubscriptionStatus
    ) : SubscribeHttpEffectInvocation()

    data class HandshakeHttpCallEffectInvocation(
        val subscriptionStatus: SubscriptionStatus
    ) : SubscribeHttpEffectInvocation()
}

data class CancelEffectInvocation(val idToCancel: String) : SubscribeEffectInvocation()

data class ScheduleRetry(val retryableEffect: SubscribeHttpEffectInvocation, val retryCount: Int) :
    SubscribeEffectInvocation()
