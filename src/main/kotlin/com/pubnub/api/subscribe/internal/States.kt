package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.State

sealed class SubscribeState : State<SubscribeEffectInvocation> {
    abstract val status: SubscriptionStatus
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf()
    override fun onExit(): Collection<SubscribeEffectInvocation> = listOf()
}

object Unsubscribed : SubscribeState() {
    override val status: SubscriptionStatus = SubscriptionStatus()
}

data class Receiving(
    override val status: SubscriptionStatus,
    val call: SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation = SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation(
        status
    ),
) : SubscribeState() {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class Handshaking(
    override val status: SubscriptionStatus,
    val retryCount: Int = 0
) : SubscribeState() {
    val call: ScheduleRetry = ScheduleRetry(
        retryableEffect = SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation(
            status
        ), retryCount = retryCount
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class Reconnecting(
    override val status: SubscriptionStatus,
    val retryCount: Int = 1
) : SubscribeState() {
    val call: ScheduleRetry = ScheduleRetry(
        retryableEffect = SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation(
            status
        ), retryCount = retryCount
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class ReconnectingFailed(
    override val status: SubscriptionStatus,
) : SubscribeState()


data class HandshakingFailed(
    override val status: SubscriptionStatus,
) : SubscribeState()
