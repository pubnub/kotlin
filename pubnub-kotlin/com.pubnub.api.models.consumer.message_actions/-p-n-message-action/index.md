//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.message_actions](../index.md)/[PNMessageAction](index.md)

# PNMessageAction

[jvm]\
open class [PNMessageAction](index.md)(val type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val messageTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))

Concrete implementation of a message action.

Add or remove actions on published messages to build features like receipts, reactions or to associate custom metadata to messages.

Clients can subscribe to a channel to receive message action events on that channel. They can also fetch past message actions from PubNub Storage independently or when they fetch original messages.

## Constructors

| | |
|---|---|
| [PNMessageAction](-p-n-message-action.md) | [jvm]<br>fun [PNMessageAction](-p-n-message-action.md)(type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), messageTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [actionTimetoken](action-timetoken.md) | [jvm]<br>var [actionTimetoken](action-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null<br>Timestamp when the message action was created. |
| [messageTimetoken](message-timetoken.md) | [jvm]<br>val [messageTimetoken](message-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Timestamp when the actual message was created the message action belongs to. |
| [type](type.md) | [jvm]<br>val [type](type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Message action's type. |
| [uuid](uuid.md) | [jvm]<br>var [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>Message action's author. |
| [value](value.md) | [jvm]<br>val [value](value.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Message action's value. |

## Inheritors

| Name |
|---|
| [PNAddMessageActionResult](../-p-n-add-message-action-result/index.md) |
