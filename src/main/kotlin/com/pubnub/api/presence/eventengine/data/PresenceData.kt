package com.pubnub.api.presence.eventengine.data

internal class PresenceData {
    internal val channels: MutableSet<String> = mutableSetOf()
    internal val channelGroups: MutableSet<String> = mutableSetOf()
}
