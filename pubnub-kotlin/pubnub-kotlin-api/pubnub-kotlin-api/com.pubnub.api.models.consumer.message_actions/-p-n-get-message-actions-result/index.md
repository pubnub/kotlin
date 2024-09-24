//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.models.consumer.message_actions](../index.md)/[PNGetMessageActionsResult](index.md)

# PNGetMessageActionsResult

[common]\
class [PNGetMessageActionsResult](index.md)(val actions: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNMessageAction](../-p-n-message-action/index.md)&gt;, val page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?)

Result for the GetMessageActions API operation.

## Constructors

| | |
|---|---|
| [PNGetMessageActionsResult](-p-n-get-message-actions-result.md) | [common]<br>constructor(actions: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNMessageAction](../-p-n-message-action/index.md)&gt;, page: [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [actions](actions.md) | [common]<br>val [actions](actions.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNMessageAction](../-p-n-message-action/index.md)&gt;<br>List of message actions for a certain message in a certain channel. |
| [page](page.md) | [common]<br>val [page](page.md): [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?<br>Information about next page. When null there's no next page. |
