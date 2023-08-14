//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.message_actions](../index.md)/[PNAddMessageActionResult](index.md)

# PNAddMessageActionResult

[jvm]\
class [PNAddMessageActionResult](index.md) : [PNMessageAction](../-p-n-message-action/index.md)

Result for the [PubNub.addMessageAction](../../com.pubnub.api/-pub-nub/add-message-action.md) API operation.

Essentially a wrapper around [PNMessageAction](../-p-n-message-action/index.md).

## Properties

| Name | Summary |
|---|---|
| [actionTimetoken](../-p-n-message-action/action-timetoken.md) | [jvm]<br>var [actionTimetoken](../-p-n-message-action/action-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null<br>Timestamp when the message action was created. |
| [messageTimetoken](../-p-n-message-action/message-timetoken.md) | [jvm]<br>val [messageTimetoken](../-p-n-message-action/message-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Timestamp when the actual message was created the message action belongs to. |
| [type](../-p-n-message-action/type.md) | [jvm]<br>val [type](../-p-n-message-action/type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Message action's type. |
| [uuid](../-p-n-message-action/uuid.md) | [jvm]<br>var [uuid](../-p-n-message-action/uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>Message action's author. |
| [value](../-p-n-message-action/value.md) | [jvm]<br>val [value](../-p-n-message-action/value.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Message action's value. |
