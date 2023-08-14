//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNHistoryItemResult](index.md)

# PNHistoryItemResult

[jvm]\
data class [PNHistoryItemResult](index.md)(val entry: JsonElement, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val meta: JsonElement? = null)

Encapsulates a message in terms of a history entry.

## Constructors

| | |
|---|---|
| [PNHistoryItemResult](-p-n-history-item-result.md) | [jvm]<br>fun [PNHistoryItemResult](-p-n-history-item-result.md)(entry: JsonElement, timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, meta: JsonElement? = null) |

## Properties

| Name | Summary |
|---|---|
| [entry](entry.md) | [jvm]<br>val [entry](entry.md): JsonElement<br>The actual message content. |
| [meta](meta.md) | [jvm]<br>val [meta](meta.md): JsonElement? = null<br>Metadata of the message, if requested via [History.includeMeta](../../com.pubnub.api.endpoints/-history/include-meta.md). Is `null` if not requested, otherwise an empty string if requested but no associated metadata. |
| [timetoken](timetoken.md) | [jvm]<br>val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null<br>Publish timetoken of the message, if requested via [History.includeTimetoken](../../com.pubnub.api.endpoints/-history/include-timetoken.md) |
