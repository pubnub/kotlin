package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.State

sealed class SubscribeState : State<SubscribeEffectInvocation, SubscriptionStatus> {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf()
    override fun onExit(): Collection<SubscribeEffectInvocation> = listOf()
}

object Unsubscribed : SubscribeState() {
    override val extendedState: SubscriptionStatus = SubscriptionStatus()
}

data class Receiving(
    override val extendedState: SubscriptionStatus,
    val call: SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation = SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation(
        extendedState
    ),

) : SubscribeState() {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class Handshaking(
    override val extendedState: SubscriptionStatus,
    val retryCount: Int = 0
) : SubscribeState() {
    val call: ScheduleRetry = ScheduleRetry(
        retryableEffect = SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation(
            extendedState
        ), retryCount = retryCount
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class Reconnecting(
    override val extendedState: SubscriptionStatus,
    val retryCount: Int = 1
) : SubscribeState() {
    val call: ScheduleRetry = ScheduleRetry(
        retryableEffect = SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation(
            extendedState
        ), retryCount = retryCount
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class ReconnectingFailed(
    override val extendedState: SubscriptionStatus,
) : SubscribeState()


data class HandshakingFailed(
    override val extendedState: SubscriptionStatus,
) : SubscribeState()
