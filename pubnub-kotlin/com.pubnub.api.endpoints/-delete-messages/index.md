//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[DeleteMessages](index.md)

# DeleteMessages

[jvm]\
class [DeleteMessages](index.md) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[Void](https://docs.oracle.com/javase/8/docs/api/java/lang/Void.html), [PNDeleteMessagesResult](../../com.pubnub.api.models.consumer.history/-p-n-delete-messages-result/index.md)&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/delete-messages.md) |  |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#-1251910896%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#-1251910896%2FFunctions%2F-1216412040)(callback: (result: [PNDeleteMessagesResult](../../com.pubnub.api.models.consumer.history/-p-n-delete-messages-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType.PNDeleteMessagesOperation](../../com.pubnub.api.enums/-p-n-operation-type/-p-n-delete-messages-operation/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNDeleteMessagesResult](../../com.pubnub.api.models.consumer.history/-p-n-delete-messages-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | [jvm]<br>val [channels](channels.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [end](end.md) | [jvm]<br>val [end](end.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null |
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
| [start](start.md) | [jvm]<br>val [start](start.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null |
