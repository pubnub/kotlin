package com.pubnub.api.presence.internal

import com.pubnub.api.state.Effect

sealed class PresenceEffect(override val child: PresenceEffect? = null) : Effect()

sealed class PresenceHttpEffect : PresenceEffect()

data class IAmHereEffect(
    val channels: Collection<String>,
    val channelGroups: Collection<String>
) : PresenceHttpEffect()

data class IAmAwayEffect(
    val channels: Collection<String>,
    val channelGroups: Collection<String>
) : PresenceHttpEffect()

data class TimerEffect(
    val event: PresenceEvent
) : PresenceEffect()

data class NewState(val name: String) : PresenceEffect()

data class CancelEffect(val idToCancel: String) : PresenceEffect()
