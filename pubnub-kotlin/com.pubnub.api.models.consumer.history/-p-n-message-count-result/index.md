//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNMessageCountResult](index.md)

# PNMessageCountResult

[jvm]\
class [PNMessageCountResult](index.md)(val channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)&gt;)

Result of the [PubNub.messageCounts](../../com.pubnub.api/-pub-nub/message-counts.md) operation.

## Constructors

| | |
|---|---|
| [PNMessageCountResult](-p-n-message-count-result.md) | [jvm]<br>fun [PNMessageCountResult](-p-n-message-count-result.md)(channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)&gt;<br>A map with values of Long for each channel. Channels without messages have a count of 0. Channels with 10,000 messages or more have a count of `10000`. |
