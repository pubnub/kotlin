package com.pubnub.api.presence.internal

import com.pubnub.api.state.Effect

sealed class PresenceEffect(override val child: PresenceEffect? = null) : Effect()

data class IAmHereEffect(
    val channels: Collection<String>,
    val channelGroups: Collection<String>
) : PresenceEffect()

data class IAmAwayEffect(
    val channels: Collection<String>,
    val channelGroups: Collection<String>
) : PresenceEffect()

data class TimerEffect(
    val event: PresenceEvent
) : PresenceEffect()

data class NewState(val name: String) : PresenceEffect()

data class CancelEffect(val idToCancel: String) : PresenceEffect()
