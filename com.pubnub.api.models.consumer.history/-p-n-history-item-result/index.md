[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.history](../index.md) / [PNHistoryItemResult](./index.md)

# PNHistoryItemResult

`class PNHistoryItemResult`

Encapsulates a message in terms of a history entry.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Encapsulates a message in terms of a history entry.`PNHistoryItemResult(entry: JsonElement, timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, meta: JsonElement? = null)` |

### Properties

| Name | Summary |
|---|---|
| [entry](entry.md) | The actual message content.`val entry: JsonElement` |
| [meta](meta.md) | Metadata of the message, if requested via [History.includeMeta](../../com.pubnub.api.endpoints/-history/include-meta.md). Is `null` if not requested, otherwise an empty string if requested but no associated metadata.`val meta: JsonElement?` |
| [timetoken](timetoken.md) | Publish timetoken of the message, if requested via [History.includeTimetoken](../../com.pubnub.api.endpoints/-history/include-timetoken.md)`val timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
