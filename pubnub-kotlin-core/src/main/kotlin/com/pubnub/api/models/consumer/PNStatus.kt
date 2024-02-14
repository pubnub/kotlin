package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException

sealed interface PNStatus {
    val isError: Boolean
        get() = false

    data class Connected(
        val currentTimetoken: Long,
        val channels: Collection<String> = emptyList(),
        val channelGroups: Collection<String> = emptyList()
    ) : PNStatus

    data class SubscriptionChanged(
        val currentTimetoken: Long,
        val channels: Collection<String> = emptyList(),
        val channelGroups: Collection<String> = emptyList()
    ) : PNStatus

    data class Reconnected(
        val currentTimetoken: Long,
        val channels: Collection<String> = emptyList(),
        val channelGroups: Collection<String> = emptyList()
    ) : PNStatus

    data class UnexpectedDisconnect(
        val exception: PubNubException
    ) : PNStatus {
        override val isError: Boolean = true
    }

    object Disconnected : PNStatus

    data class ConnectionError(
        val exception: PubNubException
    ) : PNStatus {
        override val isError: Boolean = true
    }

    data class Leave(
        val channels: List<String>,
        val channelGroups: List<String>
    ) : PNStatus

    data class HeartbeatFailed(
        val exception: PubNubException
    ) : PNStatus {
        override val isError: Boolean = true
    }

    object HeartbeatSuccess : PNStatus

    data class MalformedMessage(
        val exception: PubNubException
    ) : PNStatus {
        override val isError: Boolean = true
    }
}
