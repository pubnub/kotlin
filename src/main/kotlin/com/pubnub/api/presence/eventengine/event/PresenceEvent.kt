package com.pubnub.api.presence.eventengine.event

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.Event

internal sealed class PresenceEvent : Event {
    data class Joined(val channels: Set<String>, val channelGroups: Set<String>) : PresenceEvent()
    data class Left(val channels: Set<String>, val channelGroups: Set<String>) : PresenceEvent()

    object Reconnect : PresenceEvent()
    object Disconnect : PresenceEvent()
    object LeftAll : PresenceEvent()
    object TimesUp : PresenceEvent() // End of waiting period between heartbeats. It's time for next heartbeat.

    object HeartbeatSuccess : PresenceEvent()
    data class HeartbeatFailure(val reason: PubNubException) : PresenceEvent()
    data class HeartbeatGiveup(val reason: PubNubException) : PresenceEvent()

    // todo how to from setPresenceState operation call Presence EE??
    data class StateSet(val channels: Set<String>, val channelGroups: Set<String>) : PresenceEvent()
}
