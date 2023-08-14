//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.endpoints.objects.uuid](../index.md)/[RemoveUUIDMetadata](index.md)

# RemoveUUIDMetadata

[jvm]\
class [RemoveUUIDMetadata](index.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) : [Endpoint](../../com.pubnub.api/-endpoint/index.md)&lt;[EntityEnvelope](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;, [PNRemoveMetadataResult](../../com.pubnub.api.models.consumer.objects/-p-n-remove-metadata-result/index.md)&gt;

## Constructors

| | |
|---|---|
| [RemoveUUIDMetadata](-remove-u-u-i-d-metadata.md) | [jvm]<br>fun [RemoveUUIDMetadata](-remove-u-u-i-d-metadata.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) |

## Functions

| Name | Summary |
|---|---|
| [async](index.md#1239574100%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [async](index.md#1239574100%2FFunctions%2F-1216412040)(callback: (result: [PNRemoveMetadataResult](../../com.pubnub.api.models.consumer.objects/-p-n-remove-metadata-result/index.md)?, status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Executes the call asynchronously. This function does not block the thread. |
| [operationType](operation-type.md) | [jvm]<br>open override fun [operationType](operation-type.md)(): [PNOperationType](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [retry](../../com.pubnub.api/-endpoint/retry.md) | [jvm]<br>open override fun [retry](../../com.pubnub.api/-endpoint/retry.md)() |
| [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md) | [jvm]<br>open override fun [silentCancel](../../com.pubnub.api/-endpoint/silent-cancel.md)()<br>Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops. |
| [sync](../../com.pubnub.api/-endpoint/sync.md) | [jvm]<br>open override fun [sync](../../com.pubnub.api/-endpoint/sync.md)(): [PNRemoveMetadataResult](../../com.pubnub.api.models.consumer.objects/-p-n-remove-metadata-result/index.md)?<br>Executes the call synchronously. This function blocks the thread. |

## Properties

| Name | Summary |
|---|---|
| [queryParam](../../com.pubnub.api/-endpoint/query-param.md) | [jvm]<br>val [queryParam](../../com.pubnub.api/-endpoint/query-param.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Key-value object to pass with every PubNub API operation. Used for debugging purposes. todo: it should be removed! |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null |
