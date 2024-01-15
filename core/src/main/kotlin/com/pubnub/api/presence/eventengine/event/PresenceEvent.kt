package com.pubnub.api.presence.eventengine.event

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.Event

internal sealed class PresenceEvent : Event {
    class Joined(channels: Set<String>, channelGroups: Set<String>) : PresenceEvent() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()
    }

    class Left(channels: Set<String>, channelGroups: Set<String>) : PresenceEvent() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()
    }

    object Reconnect : PresenceEvent()
    object Disconnect : PresenceEvent()
    object LeftAll : PresenceEvent()
    object TimesUp : PresenceEvent() // End of waiting period between heartbeats. It's time for next heartbeat.

    object HeartbeatSuccess : PresenceEvent()
    data class HeartbeatFailure(val reason: PubNubException) : PresenceEvent()
    data class HeartbeatGiveup(val reason: PubNubException) : PresenceEvent()
}
