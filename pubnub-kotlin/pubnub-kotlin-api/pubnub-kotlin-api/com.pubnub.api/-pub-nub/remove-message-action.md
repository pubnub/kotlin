//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[removeMessageAction](remove-message-action.md)

# removeMessageAction

[common]\
expect abstract fun [removeMessageAction](remove-message-action.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), messageTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), actionTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)): [RemoveMessageAction](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.md)actual abstract fun [removeMessageAction](remove-message-action.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), messageTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), actionTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)): [RemoveMessageAction](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.md)

[jvm]\
actual abstract fun [removeMessageAction](remove-message-action.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), messageTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), actionTimetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)): [RemoveMessageAction](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.md)

Remove a previously added action on a published message. Returns an empty response.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel to remove message actions from. |
| messageTimetoken | The publish timetoken of the original message. |
| actionTimetoken | The publish timetoken of the message action to be removed. |
