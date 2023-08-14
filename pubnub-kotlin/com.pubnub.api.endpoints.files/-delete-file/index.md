//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.files](../index.md)/[DeleteFile](index.md)

# DeleteFile

[jvm]\
class [DeleteFile](index.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), pubNub: [PubNub](../../com.pubnub.api/-pub-nub/index.md)) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), [PNDeleteFileResult](../../com.pubnub.api.models.consumer.files/-p-n-delete-file-result/index.md)?&gt;

## See also

jvm

| | |
|---|---|
| [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/delete-file.md) |  |

## Constructors

| | |
|---|---|
| [DeleteFile](-delete-file.md) | [jvm]<br>fun [DeleteFile](-delete-file.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), pubNub: [PubNub](../../com.pubnub.api/-pub-nub/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#2114223363%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#2114223363%2FFunctions%2F-1216412040)(callback: (result: [PNDeleteFileResult](../../com.pubnub.api.models.consumer.files/-p-n-delete-file-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNDeleteFileResult](../../com.pubnub.api.models.consumer.files/-p-n-delete-file-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
