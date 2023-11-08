//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNHistoryItemResult](index.md)

# PNHistoryItemResult

[jvm]\
data class [PNHistoryItemResult](index.md)(val entry: JsonElement, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val meta: JsonElement? = null, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null)

Encapsulates a message in terms of a history entry.

## Constructors

| | |
|---|---|
| [PNHistoryItemResult](-p-n-history-item-result.md) | [jvm]<br>fun [PNHistoryItemResult](-p-n-history-item-result.md)(entry: JsonElement, timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, meta: JsonElement? = null, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [entry](entry.md) | [jvm]<br>val [entry](entry.md): JsonElement<br>The actual message content. |
| [error](error.md) | [jvm]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null<br>The error associated with message retrieval, if any. e.g. a message is unencrypted but PubNub instance is configured with the Crypto so PubNub can't decrypt the unencrypted message and return the message. |
| [meta](meta.md) | [jvm]<br>val [meta](meta.md): JsonElement? = null<br>Metadata of the message, if requested via [History.includeMeta](../../com.pubnub.api.endpoints/-history/include-meta.md). Is `null` if not requested, otherwise an empty string if requested but no associated metadata. |
| [timetoken](timetoken.md) | [jvm]<br>val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null<br>Publish timetoken of the message, if requested via [History.includeTimetoken](../../com.pubnub.api.endpoints/-history/include-timetoken.md) |
