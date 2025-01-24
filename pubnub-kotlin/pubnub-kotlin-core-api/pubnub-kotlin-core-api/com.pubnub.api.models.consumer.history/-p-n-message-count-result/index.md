//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNMessageCountResult](index.md)

# PNMessageCountResult

[common]\
class [PNMessageCountResult](index.md)(val channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;)

Result of the MessageCounts operation.

## Constructors

| | |
|---|---|
| [PNMessageCountResult](-p-n-message-count-result.md) | [common]<br>constructor(channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [common]<br>val [channels](channels.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br>A map with values of Long for each channel. Channels without messages have a count of 0. Channels with 10,000 messages or more have a count of `10000`. |
