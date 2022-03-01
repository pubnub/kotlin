package com.pubnub.api.presence.internal

import com.pubnub.api.state.State

sealed class PresenceState : State<PresenceEffect> {
    abstract val status: PresenceStatus
    override fun onEntry(): Collection<PresenceEffect> = listOf()
    override fun onExit(): Collection<PresenceEffect> = listOf()
}

object Unsubscribed : PresenceState() {
    override val status: PresenceStatus = PresenceStatus()
}

data class Waiting(
    override val status: PresenceStatus,
    val timer: TimerEffect = TimerEffect(HeartbeatIntervalOver),
) : PresenceState() {
    override fun onEntry(): Collection<PresenceEffect> = listOf(timer)
}

data class Notify(
    override val status: PresenceStatus,
    val call: IAmHereEffect = IAmHereEffect(channels = status.channels, channelGroups = status.groups)
) : PresenceState() {
    override fun onEntry(): Collection<PresenceEffect> = listOf(call)
}
