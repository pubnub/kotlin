//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.presence](../index.md)/[SetState](index.md)

# SetState

[jvm]\
class [SetState](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[Envelope](../../com.pubnub.api.models.server/-envelope/index.md)&lt;JsonElement&gt;, [PNSetStateResult](../../com.pubnub.api.models.consumer.presence/-p-n-set-state-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/set-presence-state.md) |  |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#56148981%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#56148981%2FFunctions%2F-1216412040)(callback: (result: [PNSetStateResult](../../com.pubnub.api.models.consumer.presence/-p-n-set-state-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType.PNSetStateOperation](../../com.pubnub.api.enums/-p-n-operation-type/-p-n-set-state-operation/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNSetStateResult](../../com.pubnub.api.models.consumer.presence/-p-n-set-state-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | [jvm]<br>val [channelGroups](channel-groups.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
| [state](state.md) | [jvm]<br>val [state](state.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
