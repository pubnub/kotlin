//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNHistoryItemResult](index.md)

# PNHistoryItemResult

[common]\
data class [PNHistoryItemResult](index.md)(val entry: [JsonElement](../../com.pubnub.api/-json-element/index.md), val timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null, val meta: [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null, val customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null)

Encapsulates a message in terms of a history entry.

## Constructors

| | |
|---|---|
| [PNHistoryItemResult](-p-n-history-item-result.md) | [common]<br>constructor(entry: [JsonElement](../../com.pubnub.api/-json-element/index.md), timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null, meta: [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null, customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [customMessageType](custom-message-type.md) | [common]<br>val [customMessageType](custom-message-type.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [entry](entry.md) | [common]<br>val [entry](entry.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)<br>The actual message content. |
| [error](error.md) | [common]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null<br>The error associated with message retrieval, if any. e.g. a message is unencrypted but PubNub instance is configured with the Crypto so PubNub can't decrypt the unencrypted message and return the message. |
| [meta](meta.md) | [common]<br>val [meta](meta.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null<br>Metadata of the message, if requested via History.includeMeta. Is `null` if not requested, otherwise an empty string if requested but no associated metadata. |
| [timetoken](timetoken.md) | [common]<br>val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? = null<br>Publish timetoken of the message, if requested via History.includeTimetoken |
