package com.pubnub.api.presence.internal

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.state.Event

sealed class PresenceEvent : Event

object InitialEvent : PresenceEvent()

object HeartbeatIntervalOver : PresenceEvent()

sealed class IAmHere : PresenceEvent() {
    object Succeed : IAmHere()
    data class Failed(val status: PNStatus) : IAmHere()
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

