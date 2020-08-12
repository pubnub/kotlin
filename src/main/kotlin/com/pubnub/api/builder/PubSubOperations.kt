package com.pubnub.api.builder

// basic publish/subscribe class

internal abstract class PubSubOperation(
    internal val channels: List<String> = emptyList(),
    internal val channelGroups: List<String> = emptyList()
)

// concrete publish/subscribe cases

internal class SubscribeOperation(
    channels: List<String> = emptyList(),
    channelGroups: List<String> = emptyList(),
    internal val presenceEnabled: Boolean = false,
    internal val timetoken: Long = 0L
) : PubSubOperation(channels, channelGroups)

internal class UnsubscribeOperation(
    channels: List<String> = emptyList(),
    channelGroups: List<String> = emptyList()
) : PubSubOperation(channels, channelGroups)

internal class PresenceOperation(
    channels: List<String> = emptyList(),
    channelGroups: List<String> = emptyList(),
    internal val connected: Boolean = false
) : PubSubOperation(channels, channelGroups)

internal class StateOperation(
    channels: List<String> = emptyList(),
    channelGroups: List<String> = emptyList(),
    val state: Any? = null
) : PubSubOperation(channels, channelGroups)
