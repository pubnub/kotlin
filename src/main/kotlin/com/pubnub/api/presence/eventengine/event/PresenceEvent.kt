package com.pubnub.api.presence.eventengine.event

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.Event

sealed class PresenceEvent : Event {
    data class Joined(val channels: Set<String>, val channelGroups: Set<String>) : PresenceEvent()
    data class Left(val channels: Set<String>, val channelGroups: Set<String>) : PresenceEvent()

    object Reconnect : PresenceEvent()
    object Disconnect : PresenceEvent()
    object LeftAll : PresenceEvent()
    object NextHeartbeat : PresenceEvent()

    object HeartbeatSuccess : PresenceEvent()
    data class HeartbeatFailure(val reason: PubNubException) : PresenceEvent()
    data class HeartbeatGiveup(val reason: PubNubException) : PresenceEvent()

    data class StateSet(val channels: Set<String>, val channelGroups: Set<String>) : PresenceEvent()
}
