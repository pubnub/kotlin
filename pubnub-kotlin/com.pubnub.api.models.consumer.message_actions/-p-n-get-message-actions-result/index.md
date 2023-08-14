//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.message_actions](../index.md)/[PNGetMessageActionsResult](index.md)

# PNGetMessageActionsResult

[jvm]\
class [PNGetMessageActionsResult](index.md)

Result for the [PubNub.getMessageActions](../../com.pubnub.api/-pub-nub/get-message-actions.md) API operation.

## Properties

| Name | Summary |
|---|---|
| [actions](actions.md) | [jvm]<br>@SerializedName(value = &quot;data&quot;)<br>val [actions](actions.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PNMessageAction](../-p-n-message-action/index.md)&gt;<br>List of message actions for a certain message in a certain channel. |
| [page](page.md) | [jvm]<br>@SerializedName(value = &quot;more&quot;)<br>val [page](page.md): [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md)?<br>Information about next page. When null there's no next page. |
