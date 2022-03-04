package com.pubnub.api.presence.internal

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.state.Event
import com.pubnub.api.subscribe.internal.Cursor
import com.pubnub.api.subscribe.internal.SubscribeEvent

sealed class PresenceEvent : Event

object InitialEvent : PresenceEvent()

object HeartbeatIntervalOver : PresenceEvent()

sealed class IAmHere : PresenceEvent() {
    object Succeed : IAmHere()
    data class Failed(val status: PNStatus) : IAmHere()
}

sealed class IAmAway : PresenceEvent() {
    object Succeed : IAmAway()
    data class Failed(val status: PNStatus) : IAmAway()
}

sealed class Commands : PresenceEvent() {
    data class SubscribeIssued(
        val channels: List<String>,
        val groups: List<String> = listOf(),
    ) : Commands()

    data class UnsubscribeIssued(
        val channels: List<String>,
        val groups: List<String> = listOf()
    ) : Commands()

    object UnsubscribeAllIssued : Commands()
}



