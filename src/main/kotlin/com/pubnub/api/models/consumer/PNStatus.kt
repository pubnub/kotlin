package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException

sealed interface PNStatus {
    data class Connected (
        val currentTimetoken: Long,
        val channels: List<String> = emptyList(),
        val channelGroups: List<String> = emptyList()
    ) : PNStatus

    data class SubscriptionChanged(
        val currentTimetoken: Long,
        val channels: List<String?> = emptyList(),
        val channelGroups: List<String?> = emptyList()
    ) : PNStatus

    data class Reconnected (
        val currentTimetoken: Long,
        val channels: List<String> = emptyList(),
        val channelGroups: List<String> = emptyList()
    ) : PNStatus

    data class UnexpectedDisconnect(
        val exception: PubNubException
    ) : PNStatus

    object Disconnected : PNStatus

    data class ConnectionError(
        val exception: PubNubException
    ) : PNStatus

    data class Leave(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : PNStatus

    data class HeartbeatFailed(
        val exception: PubNubException
    ) : PNStatus

    object HeartbeatSuccess : PNStatus

    data class MalformedMessage(
        val exception: PubNubException
    ) : PNStatus
}
