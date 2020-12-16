[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.history](../index.md) / [PNFetchMessageItem](index.md) / [actions](./actions.md)

# actions

`val actions: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Action`](../-action/index.md)`>>>?`

The message actions associated with the message.
Is `null` if not requested via [FetchMessages.includeMessageActions](../../com.pubnub.api.endpoints/-fetch-messages/include-message-actions.md).
The key of the map is the action type. The value is another map,
which key is the actual value of the message action,
and the key being a list of actions, ie. a list of UUIDs which have posted such a message action.

### Property

`actions` - The message actions associated with the message.
Is `null` if not requested via [FetchMessages.includeMessageActions](../../com.pubnub.api.endpoints/-fetch-messages/include-message-actions.md).
The key of the map is the action type. The value is another map,
which key is the actual value of the message action,
and the key being a list of actions, ie. a list of UUIDs which have posted such a message action.

**See Also**

[Action](../-action/index.md)

