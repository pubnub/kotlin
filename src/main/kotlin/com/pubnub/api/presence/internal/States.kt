package com.pubnub.api.presence.internal

import com.pubnub.api.state.State

sealed class PresenceState : State<PresenceEffectInvocation, PresenceExtendedState> {
    override fun onEntry(): Collection<PresenceEffectInvocation> = listOf()
    override fun onExit(): Collection<PresenceEffectInvocation> = listOf()
}

object Unsubscribed : PresenceState() {
    override val extendedState: PresenceExtendedState = PresenceExtendedState()
}

data class Waiting(
    override val extendedState: PresenceExtendedState,
    val timer: TimerEffectInvocation = TimerEffectInvocation(HeartbeatIntervalOver),
) : PresenceState() {
    override fun onEntry(): Collection<PresenceEffectInvocation> = listOf(timer)
}

data class Notify(
    override val extendedState: PresenceExtendedState,
    val call: IAmHereEffectInvocation = IAmHereEffectInvocation(channels = extendedState.channels, channelGroups = extendedState.groups)
) : PresenceState() {
    override fun onEntry(): Collection<PresenceEffectInvocation> = listOf(call)
}
