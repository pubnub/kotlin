//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[messageCounts](message-counts.md)

# messageCounts

[common]\
expect abstract fun [messageCounts](message-counts.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelsTimetoken: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;): [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md)actual abstract fun [messageCounts](message-counts.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelsTimetoken: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;): [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md)

[jvm]\
actual abstract fun [messageCounts](message-counts.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;, channelsTimetoken: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;): [MessageCounts](../../com.pubnub.api.endpoints/-message-counts/index.md)

Fetches the number of messages published on one or more channels since a given time. The count returned is the number of messages in history with a timetoken value greater than the passed value in the [MessageCounts.channelsTimetoken](../../com.pubnub.api.endpoints/-message-counts/channels-timetoken.md) parameter.

#### Parameters

jvm

| | |
|---|---|
| channels | Channels to fetch the message count from. |
| channelsTimetoken | List of timetokens, in order of the channels list.     Specify a single timetoken to apply it to all channels.     Otherwise, the list of timetokens must be the same length as the list of channels. |
