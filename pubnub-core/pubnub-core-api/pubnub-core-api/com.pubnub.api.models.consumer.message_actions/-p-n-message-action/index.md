//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.message_actions](../index.md)/[PNMessageAction](index.md)

# PNMessageAction

open class [PNMessageAction](index.md)(var type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), var value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), var messageTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))

Concrete implementation of a message action.

Add or remove actions on published messages to build features like receipts, reactions or to associate custom metadata to messages.

Clients can subscribe to a channel to receive message action events on that channel. They can also fetch past message actions from PubNub Storage independently or when they fetch original messages.

#### Inheritors

| |
|---|
| [PNAddMessageActionResult](../-p-n-add-message-action-result/index.md) |

## Constructors

| | |
|---|---|
| [PNMessageAction](-p-n-message-action.md) | [jvm]<br>constructor()constructor(type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), messageTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [actionTimetoken](action-timetoken.md) | [jvm]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [actionTimetoken](action-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?<br>Timestamp when the message action was created. |
| [messageTimetoken](message-timetoken.md) | [jvm]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [messageTimetoken](message-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Timestamp when the actual message was created the message action belongs to. |
| [type](type.md) | [jvm]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [type](type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Message action's type. |
| [uuid](uuid.md) | [jvm]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>Message action's author. |
| [value](value.md) | [jvm]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [value](value.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Message action's value. |

## Functions

| Name | Summary |
|---|---|
| [getActionTimetoken](get-action-timetoken.md) | [jvm]<br>fun [getActionTimetoken](get-action-timetoken.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [getMessageTimetoken](get-message-timetoken.md) | [jvm]<br>fun [getMessageTimetoken](get-message-timetoken.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [getType](get-type.md) | [jvm]<br>fun [getType](get-type.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getUuid](get-uuid.md) | [jvm]<br>fun [getUuid](get-uuid.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [getValue](get-value.md) | [jvm]<br>fun [getValue](get-value.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [setActionTimetoken](set-action-timetoken.md) | [jvm]<br>fun [setActionTimetoken](set-action-timetoken.md)(actionTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [PNMessageAction](index.md) |
| [setMessageTimetoken](set-message-timetoken.md) | [jvm]<br>fun [setMessageTimetoken](set-message-timetoken.md)(messageTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [PNMessageAction](index.md) |
| [setType](set-type.md) | [jvm]<br>fun [setType](set-type.md)(type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNMessageAction](index.md) |
| [setUuid](set-uuid.md) | [jvm]<br>fun [setUuid](set-uuid.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNMessageAction](index.md) |
| [setValue](set-value.md) | [jvm]<br>fun [setValue](set-value.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNMessageAction](index.md) |
