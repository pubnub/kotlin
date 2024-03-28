//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[PNFetchMessageItem](index.md)

# PNFetchMessageItem

data class [PNFetchMessageItem](index.md)(val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, val message: JsonElement, val meta: JsonElement?, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, val actions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem.Action](-action/index.md)&gt;&gt;&gt;? = null, val messageType: [HistoryMessageType](../-history-message-type/index.md)?, val error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null)

Encapsulates a message in terms of a batch history entry.

#### See also

| |
|---|
| [PNFetchMessageItem.Action](-action/index.md) |

## Constructors

| | |
|---|---|
| [PNFetchMessageItem](-p-n-fetch-message-item.md) | [jvm]<br>constructor(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, message: JsonElement, meta: JsonElement?, timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?, actions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem.Action](-action/index.md)&gt;&gt;&gt;? = null, messageType: [HistoryMessageType](../-history-message-type/index.md)?, error: [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null) |

## Types

| Name | Summary |
|---|---|
| [Action](-action/index.md) | [jvm]<br>class [Action](-action/index.md)(val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val actionTimetoken: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Action](../-action/index.md) |

## Properties

| Name | Summary |
|---|---|
| [actions](actions.md) | [jvm]<br>val [actions](actions.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNFetchMessageItem.Action](-action/index.md)&gt;&gt;&gt;? = null<br>The message actions associated with the message. Is `null` if not requested via `includeMessageActions`. The key of the map is the action type. The value is another map, which key is the actual value of the message action, and the key being a list of actions, i.e. a list of UUIDs which have posted such a message action. |
| [error](error.md) | [jvm]<br>val [error](error.md): [PubNubError](../../com.pubnub.api/-pub-nub-error/index.md)? = null<br>The error associated with message retrieval, if any. e.g. a message is unencrypted but PubNub instance is configured with the Crypto so PubNub can't decrypt the unencrypted message and return the message. |
| [message](message.md) | [jvm]<br>val [message](message.md): JsonElement<br>The actual message content. |
| [messageType](message-type.md) | [jvm]<br>val [messageType](message-type.md): [HistoryMessageType](../-history-message-type/index.md)?<br>The message type associated with the item. |
| [meta](meta.md) | [jvm]<br>val [meta](meta.md): JsonElement?<br>Metadata of the message, if requested via `includeMeta`. Is `null` if not requested, otherwise an empty string if requested but no associated metadata. |
| [timetoken](timetoken.md) | [jvm]<br>val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?<br>Publish timetoken of the message. |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>Publisher uuid. Is `null` if not requested. |
