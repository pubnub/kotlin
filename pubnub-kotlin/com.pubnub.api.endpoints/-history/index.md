//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[History](index.md)

# History

[jvm]\
class [History](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;JsonElement, [PNHistoryResult](../../com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/history.md) |  |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#387037593%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#387037593%2FFunctions%2F-1216412040)(callback: (result: [PNHistoryResult](../../com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType.PNHistoryOperation](../../com.pubnub.api.enums/-p-n-operation-type/-p-n-history-operation/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNHistoryResult](../../com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [count](count.md) | [jvm]<br>val [count](count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [end](end.md) | [jvm]<br>val [end](end.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null |
| [includeMeta](include-meta.md) | [jvm]<br>val [includeMeta](include-meta.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeTimetoken](include-timetoken.md) | [jvm]<br>val [includeTimetoken](include-timetoken.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
| [reverse](reverse.md) | [jvm]<br>val [reverse](reverse.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [start](start.md) | [jvm]<br>val [start](start.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null |
