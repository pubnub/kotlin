[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.history](../index.md) / [PNFetchMessageItem](./index.md)

# PNFetchMessageItem

`class PNFetchMessageItem`

Encapsulates a message in terms of a batch history entry.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Encapsulates a message in terms of a batch history entry.`PNFetchMessageItem(message: JsonElement, meta: JsonElement?, timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [actions](actions.md) | The message actions associated with the message. Is `null` if not requested via [FetchMessages.includeMessageActions](../../com.pubnub.api.endpoints/-fetch-messages/include-message-actions.md). The key of the map is the action type. The value is another map, which key is the actual value of the message action, and the key being a list of actions, ie. a list of UUIDs which have posted such a message action.`var actions: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Action`](../-action/index.md)`>>>?` |
| [message](message.md) | The actual message content.`val message: JsonElement` |
| [meta](meta.md) | Metadata of the message, if requested via [FetchMessages.includeMeta](../../com.pubnub.api.endpoints/-fetch-messages/include-meta.md). Is `null` if not requested, otherwise an empty string if requested but no associated metadata.`val meta: JsonElement?` |
| [timetoken](timetoken.md) | Publish timetoken of the message.`val timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
