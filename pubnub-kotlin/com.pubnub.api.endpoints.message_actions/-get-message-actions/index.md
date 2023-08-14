//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.message_actions](../index.md)/[GetMessageActions](index.md)

# GetMessageActions

[jvm]\
class [GetMessageActions](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[PNGetMessageActionsResult](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md), [PNGetMessageActionsResult](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/get-message-actions.md) |  |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-669328592%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#-669328592%2FFunctions%2F-1216412040)(callback: (result: [PNGetMessageActionsResult](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType.PNGetMessageActions](../../com.pubnub.api.enums/-p-n-operation-type/-p-n-get-message-actions/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNGetMessageActionsResult](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [page](page.md) | [jvm]<br>val [page](page.md): [PNBoundedPage](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
