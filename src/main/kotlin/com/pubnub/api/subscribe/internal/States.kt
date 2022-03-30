package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.State

sealed class SubscribeState : State<SubscribeEffectInvocation, SubscribeExtendedState> {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf()
    override fun onExit(): Collection<SubscribeEffectInvocation> = listOf()
}

object Unsubscribed : SubscribeState() {
    override val extendedState: SubscribeExtendedState = SubscribeExtendedState()
}

data class Receiving(
    override val extendedState: SubscribeExtendedState,
    val call: SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation = SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation(
        extendedState
    ),

    ) : SubscribeState() {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class Handshaking(
    override val extendedState: SubscribeExtendedState,
) : SubscribeState() {
    val call: SubscribeEffectInvocation = if (extendedState.retryCounter == 0) {
        SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation(
            extendedState
        )
    } else {
        ScheduleRetry(
            retryableEffect = SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation(
                extendedState
            ), retryCount = extendedState.retryCounter
        )
    }

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class Reconnecting(
    override val extendedState: SubscribeExtendedState,
) : SubscribeState() {
    val call: ScheduleRetry = ScheduleRetry(
        retryableEffect = SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation(
            extendedState
        ), retryCount = extendedState.retryCounter
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
}

data class ReconnectingFailed(
    override val extendedState: SubscribeExtendedState,
) : SubscribeState()


data class HandshakingFailed(
    override val extendedState: SubscribeExtendedState,
) : SubscribeState()
