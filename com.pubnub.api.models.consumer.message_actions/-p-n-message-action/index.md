[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.message_actions](../index.md) / [PNMessageAction](./index.md)

# PNMessageAction

`open class PNMessageAction`

Concrete implementation of a message action.

Add or remove actions on published messages to build features like receipts,
reactions or to associate custom metadata to messages.

Clients can subscribe to a channel to receive message action events on that channel.
They can also fetch past message actions from PubNub Storage independently or when they fetch original messages.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Concrete implementation of a message action.`PNMessageAction(type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, messageTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [actionTimetoken](action-timetoken.md) | Timestamp when the message action was created.`var actionTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [messageTimetoken](message-timetoken.md) | Timestamp when the actual message was created the message action belongs to.`val messageTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [type](type.md) | Message action's type.`val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [uuid](uuid.md) | Message action's author.`var uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [value](value.md) | Message action's value.`val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [PNAddMessageActionResult](../-p-n-add-message-action-result.md) | Result for the [PubNub.addMessageAction](../../com.pubnub.api/-pub-nub/add-message-action.md) API operation.`class PNAddMessageActionResult : `[`PNMessageAction`](./index.md) |
