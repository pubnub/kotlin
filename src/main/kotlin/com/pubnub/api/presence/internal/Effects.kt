package com.pubnub.api.presence.internal

import com.pubnub.api.state.EffectInvocation

sealed class PresenceEffectInvocation(override val child: PresenceEffectInvocation? = null) : EffectInvocation()

sealed class PresenceHttpEffectInvocation : PresenceEffectInvocation()

data class IAmHereEffectInvocation(
    val channels: Collection<String>,
    val channelGroups: Collection<String>
) : PresenceHttpEffectInvocation()

data class IAmAwayEffectInvocation(
    val channels: Collection<String>,
    val channelGroups: Collection<String>
) : PresenceHttpEffectInvocation()

data class TimerEffectInvocation(
    val event: PresenceEvent
) : PresenceEffectInvocation()

data class NewState(val name: String) : PresenceEffectInvocation()

data class CancelEffectInvocation(val idToCancel: String) : PresenceEffectInvocation()
