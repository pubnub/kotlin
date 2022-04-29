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
    val call: ReceiveEvents = ReceiveEvents(
        extendedState
    )

) : SubscribeState() {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
    override fun onExit(): Collection<SubscribeEffectInvocation> = cancel(call)
}

data class Preparing(override val extendedState: SubscribeExtendedState) : SubscribeState()
data class Paused(override val extendedState: SubscribeExtendedState) : SubscribeState()
data class HandshakeReconnecting(override val extendedState: SubscribeExtendedState) : SubscribeState() {
    val call: SubscribeEffectInvocation = HandshakeReconnect(
        extendedState
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
    override fun onExit(): Collection<SubscribeEffectInvocation> = cancel(call)
}

data class Handshaking(
    override val extendedState: SubscribeExtendedState
) : SubscribeState() {
    val call: SubscribeEffectInvocation = Handshake(
        extendedState
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
    override fun onExit(): Collection<SubscribeEffectInvocation> = cancel(call)
}

data class ReceiveReconnecting(
    override val extendedState: SubscribeExtendedState
) : SubscribeState() {
    val call = ReceiveEventsReconnect(
        extendedState
    )

    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(call)
    override fun onExit(): Collection<SubscribeEffectInvocation> = cancel(call)
}

data class ReconnectingFailed(
    override val extendedState: SubscribeExtendedState
) : SubscribeState() {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(ReconnectionFailed)
}

data class HandshakingFailed(
    override val extendedState: SubscribeExtendedState
) : SubscribeState() {
    override fun onEntry(): Collection<SubscribeEffectInvocation> = listOf(HandshakeFailed)
}
