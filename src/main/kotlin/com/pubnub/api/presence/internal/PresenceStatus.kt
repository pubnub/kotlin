package com.pubnub.api.presence.internal

import com.pubnub.api.state.ExtendedState

data class PresenceStatus(
    val channels: Collection<String> = setOf(),
    val groups: Collection<String> = setOf(),
) : ExtendedState
