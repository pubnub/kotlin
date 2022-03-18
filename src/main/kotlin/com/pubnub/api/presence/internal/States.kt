package com.pubnub.api.presence.internal

import com.pubnub.api.state.State

sealed class PresenceState : State<PresenceEffectInvocation> {
    abstract val status: PresenceStatus
    override fun onEntry(): Collection<PresenceEffectInvocation> = listOf()
    override fun onExit(): Collection<PresenceEffectInvocation> = listOf()
}

object Unsubscribed : PresenceState() {
    override val status: PresenceStatus = PresenceStatus()
}

data class Waiting(
    override val status: PresenceStatus,
    val timer: TimerEffectInvocation = TimerEffectInvocation(HeartbeatIntervalOver),
) : PresenceState() {
    override fun onEntry(): Collection<PresenceEffectInvocation> = listOf(timer)
}

data class Notify(
    override val status: PresenceStatus,
    val call: IAmHereEffectInvocation = IAmHereEffectInvocation(channels = status.channels, channelGroups = status.groups)
) : PresenceState() {
    override fun onEntry(): Collection<PresenceEffectInvocation> = listOf(call)
}
