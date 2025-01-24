//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.models.consumer.message_actions](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [PNAddMessageActionResult](-p-n-add-message-action-result/index.md) | [common]<br>class [PNAddMessageActionResult](-p-n-add-message-action-result/index.md)(action: [PNMessageAction](-p-n-message-action/index.md)) : [PNMessageAction](-p-n-message-action/index.md)<br>Result for the AddMessageAction API operation. |
| [PNGetMessageActionsResult](-p-n-get-message-actions-result/index.md) | [common]<br>class [PNGetMessageActionsResult](-p-n-get-message-actions-result/index.md)(val actions: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PNMessageAction](-p-n-message-action/index.md)&gt;, val page: [PNBoundedPage](../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?)<br>Result for the GetMessageActions API operation. |
| [PNMessageAction](-p-n-message-action/index.md) | [common]<br>open class [PNMessageAction](-p-n-message-action/index.md)(var type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), var value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), var messageTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br>Concrete implementation of a message action. |
| [PNRemoveMessageActionResult](-p-n-remove-message-action-result/index.md) | [common]<br>class [PNRemoveMessageActionResult](-p-n-remove-message-action-result/index.md)<br>Result for the RemoveMessageAction API operation. |
