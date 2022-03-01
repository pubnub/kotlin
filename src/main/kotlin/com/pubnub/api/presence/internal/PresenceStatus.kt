package com.pubnub.api.presence.internal

data class PresenceStatus(
    val channels: Collection<String> = setOf(),
    val groups: Collection<String> = setOf(),
)
