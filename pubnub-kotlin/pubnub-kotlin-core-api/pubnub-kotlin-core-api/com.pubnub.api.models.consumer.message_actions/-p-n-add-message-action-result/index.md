//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.message_actions](../index.md)/[PNAddMessageActionResult](index.md)

# PNAddMessageActionResult

[common]\
class [PNAddMessageActionResult](index.md)(action: [PNMessageAction](../-p-n-message-action/index.md)) : [PNMessageAction](../-p-n-message-action/index.md)

Result for the AddMessageAction API operation.

Essentially a wrapper around [PNMessageAction](../-p-n-message-action/index.md).

## Constructors

| | |
|---|---|
| [PNAddMessageActionResult](-p-n-add-message-action-result.md) | [common]<br>constructor(action: [PNMessageAction](../-p-n-message-action/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [actionTimetoken](../-p-n-message-action/action-timetoken.md) | [common]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [actionTimetoken](../-p-n-message-action/action-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)?<br>Timestamp when the message action was created. |
| [messageTimetoken](../-p-n-message-action/message-timetoken.md) | [common]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [messageTimetoken](../-p-n-message-action/message-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br>Timestamp when the actual message was created the message action belongs to. |
| [type](../-p-n-message-action/type.md) | [common]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [type](../-p-n-message-action/type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>Message action's type. |
| [uuid](../-p-n-message-action/uuid.md) | [common]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [uuid](../-p-n-message-action/uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?<br>Message action's author. |
| [value](../-p-n-message-action/value.md) | [common]<br>@[JvmField](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-field/index.html)<br>var [value](../-p-n-message-action/value.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>Message action's value. |

## Functions

| Name | Summary |
|---|---|
| [getActionTimetoken](../-p-n-message-action/get-action-timetoken.md) | [common]<br>fun [getActionTimetoken](../-p-n-message-action/get-action-timetoken.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)? |
| [getMessageTimetoken](../-p-n-message-action/get-message-timetoken.md) | [common]<br>fun [getMessageTimetoken](../-p-n-message-action/get-message-timetoken.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)? |
| [getType](../-p-n-message-action/get-type.md) | [common]<br>fun [getType](../-p-n-message-action/get-type.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [getUuid](../-p-n-message-action/get-uuid.md) | [common]<br>fun [getUuid](../-p-n-message-action/get-uuid.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [getValue](../-p-n-message-action/get-value.md) | [common]<br>fun [getValue](../-p-n-message-action/get-value.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [setActionTimetoken](../-p-n-message-action/set-action-timetoken.md) | [common]<br>fun [setActionTimetoken](../-p-n-message-action/set-action-timetoken.md)(actionTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [PNMessageAction](../-p-n-message-action/index.md) |
| [setMessageTimetoken](../-p-n-message-action/set-message-timetoken.md) | [common]<br>fun [setMessageTimetoken](../-p-n-message-action/set-message-timetoken.md)(messageTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [PNMessageAction](../-p-n-message-action/index.md) |
| [setType](../-p-n-message-action/set-type.md) | [common]<br>fun [setType](../-p-n-message-action/set-type.md)(type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PNMessageAction](../-p-n-message-action/index.md) |
| [setUuid](../-p-n-message-action/set-uuid.md) | [common]<br>fun [setUuid](../-p-n-message-action/set-uuid.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PNMessageAction](../-p-n-message-action/index.md) |
| [setValue](../-p-n-message-action/set-value.md) | [common]<br>fun [setValue](../-p-n-message-action/set-value.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PNMessageAction](../-p-n-message-action/index.md) |
