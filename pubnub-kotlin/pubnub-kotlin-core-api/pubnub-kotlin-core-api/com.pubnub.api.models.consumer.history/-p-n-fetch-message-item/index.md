//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNFetchMessageItem](index.md)

# PNFetchMessageItem

data class [PNFetchMessageItem](index.md)(val uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, val message: [JsonElement](../../com.pubnub.api/-json-element/index.md), val meta: [JsonElement](../../com.pubnub.api/-json-element/index.md)?, val timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?, val actions: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem.Action](-action/index.md)&gt;&gt;&gt;? = null, val messageType: [HistoryMessageType](../-history-message-type/index.md)?, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null, val customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null)

Encapsulates a message in terms of a batch history entry.

#### See also

| |
|---|
| [PNFetchMessageItem.Action](-action/index.md) |

## Constructors

| | |
|---|---|
| [PNFetchMessageItem](-p-n-fetch-message-item.md) | [common]<br>constructor(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, message: [JsonElement](../../com.pubnub.api/-json-element/index.md), meta: [JsonElement](../../com.pubnub.api/-json-element/index.md)?, timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?, actions: [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem.Action](-action/index.md)&gt;&gt;&gt;? = null, messageType: [HistoryMessageType](../-history-message-type/index.md)?, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null, customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Action](-action/index.md) | [common]<br>class [Action](-action/index.md)(val uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val actionTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)) : [Action](../-action/index.md) |

## Properties

| Name | Summary |
|---|---|
| [actions](actions.md) | [common]<br>val [actions](actions.md): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem.Action](-action/index.md)&gt;&gt;&gt;? = null<br>The message actions associated with the message. Is `null` if not requested via `includeMessageActions`. The key of the map is the action type. The value is another map, which key is the actual value of the message action, and the key being a list of actions, i.e. a list of UUIDs which have posted such a message action. |
| [customMessageType](custom-message-type.md) | [common]<br>val [customMessageType](custom-message-type.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [error](error.md) | [common]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null<br>The error associated with message retrieval, if any. e.g. a message is unencrypted but PubNub instance is configured with the Crypto so PubNub can't decrypt the unencrypted message and return the message. |
| [message](message.md) | [common]<br>val [message](message.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)<br>The actual message content. |
| [messageType](message-type.md) | [common]<br>val [messageType](message-type.md): [HistoryMessageType](../-history-message-type/index.md)?<br>The message type associated with the item. |
| [meta](meta.md) | [common]<br>val [meta](meta.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)?<br>Metadata of the message, if requested via `includeMeta`. Is `null` if not requested, otherwise an empty string if requested but no associated metadata. |
| [timetoken](timetoken.md) | [common]<br>val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?<br>Publish timetoken of the message. |
| [uuid](uuid.md) | [common]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>Publisher uuid. Is `null` if not requested. |
