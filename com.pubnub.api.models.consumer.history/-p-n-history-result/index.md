[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.history](../index.md) / [PNHistoryResult](./index.md)

# PNHistoryResult

`class PNHistoryResult`

Result of the [PubNub.history](../../com.pubnub.api/-pub-nub/history.md) operation.

### Properties

| Name | Summary |
|---|---|
| [endTimetoken](end-timetoken.md) | End timetoken of the returned list of messages.`val endTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [messages](messages.md) | List of messages as instances of [PNHistoryItemResult](../-p-n-history-item-result/index.md).`val messages: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNHistoryItemResult`](../-p-n-history-item-result/index.md)`>` |
| [startTimetoken](start-timetoken.md) | Start timetoken of the returned list of messages.`val startTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
