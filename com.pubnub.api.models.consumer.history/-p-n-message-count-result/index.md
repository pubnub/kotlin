[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.history](../index.md) / [PNMessageCountResult](./index.md)

# PNMessageCountResult

`class PNMessageCountResult`

Result of the [PubNub.messageCounts](../../com.pubnub.api/-pub-nub/message-counts.md) operation.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Result of the [PubNub.messageCounts](../../com.pubnub.api/-pub-nub/message-counts.md) operation.`PNMessageCountResult(channels: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`>)` |

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | A map with values of Long for each channel. Channels without messages have a count of 0. Channels with 10,000 messages or more have a count of `10000`.`val channels: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`>` |
