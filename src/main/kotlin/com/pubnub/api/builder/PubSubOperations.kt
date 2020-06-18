package com.pubnub.api.builder

// basic publish/subscribe class

internal abstract class PubSubOperation(
    internal var channels: List<String> = emptyList(),
    internal var channelGroups: List<String> = emptyList()
)

// concrete publish/subscribe cases

internal class SubscribeOperation(
    internal var presenceEnabled: Boolean = false,
    internal var timetoken: Long = 0L
) : PubSubOperation()

internal class UnsubscribeOperation : PubSubOperation()

internal class PresenceOperation(
    internal var connected: Boolean = false
) : PubSubOperation()

internal class StateOperation(
    var state: Any? = null
) : PubSubOperation()
