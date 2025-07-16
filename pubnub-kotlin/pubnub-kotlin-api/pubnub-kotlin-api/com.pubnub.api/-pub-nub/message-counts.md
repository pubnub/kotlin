//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[messageCounts](message-counts.md)

# messageCounts

[common]\
expect abstract fun [messageCounts](message-counts.md)(channels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelsTimetoken: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)&gt;): [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md)actual abstract fun [messageCounts](message-counts.md)(channels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelsTimetoken: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)&gt;): [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md)

[jvm]\
actual abstract fun [messageCounts](message-counts.md)(channels: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelsTimetoken: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)&gt;): [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md)

Fetches the number of messages published on one or more channels since a given time. The count returned is the number of messages in history with a timetoken value greater than the passed value in the [MessageCounts.channelsTimetoken](../../com.pubnub.api.endpoints/-message-counts/channels-timetoken.md) parameter.

**Important:** The timetoken represents an exclusive boundary. Messages with timetokens greater than (but not equal to) the specified timetoken are counted. To count messages from a specific message onwards, you typically need to subtract 1 from the message's timetoken.

#### Parameters

jvm

| | |
|---|---|
| channels | Channels to fetch the message count from. |
| channelsTimetoken | List of timetokens representing exclusive boundaries for message counting.     Each timetoken corresponds to a channel in the same order.     - **Single timetoken**: Applied to all channels (list with one element)     - **Multiple timetokens**: Must match the number of channels exactly     - **Exclusive boundary**: Only messages with timetokens specified value are counted     - **Common pattern**: Use `(messageTimetoken - 1)` to count from a specific message onwards |
