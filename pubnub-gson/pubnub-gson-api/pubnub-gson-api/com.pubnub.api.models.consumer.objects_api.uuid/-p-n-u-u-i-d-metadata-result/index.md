//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.models.consumer.objects_api.uuid](../index.md)/[PNUUIDMetadataResult](index.md)

# PNUUIDMetadataResult

[jvm]\
open class [PNUUIDMetadataResult](index.md) : [ObjectResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.md)&lt;[T](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.md)&gt; , [PubSubResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-pub-sub-result/index.md)

## Constructors

| | |
|---|---|
| [PNUUIDMetadataResult](-p-n-u-u-i-d-metadata-result.md) | [jvm]<br>constructor(event: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), data: [PNUUIDMetadata](../-p-n-u-u-i-d-metadata/index.md), channel: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), subscription: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), timetoken: [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html), userMetadata: JsonElement, publisher: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |

## Functions

| Name | Summary |
|---|---|
| [getChannel](index.md#745826242%2FFunctions%2F126356644) | [jvm]<br>abstract fun [getChannel](index.md#745826242%2FFunctions%2F126356644)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getData](index.md#1079843989%2FFunctions%2F126356644) | [jvm]<br>abstract fun [getData](index.md#1079843989%2FFunctions%2F126356644)(): [T](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.md) |
| [getEvent](index.md#1536410977%2FFunctions%2F126356644) | [jvm]<br>abstract fun [getEvent](index.md#1536410977%2FFunctions%2F126356644)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getPublisher](index.md#-1061072151%2FFunctions%2F126356644) | [jvm]<br>abstract fun [getPublisher](index.md#-1061072151%2FFunctions%2F126356644)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getSubscription](index.md#-1010911592%2FFunctions%2F126356644) | [jvm]<br>abstract fun [getSubscription](index.md#-1010911592%2FFunctions%2F126356644)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getTimetoken](index.md#1142058905%2FFunctions%2F126356644) | [jvm]<br>abstract fun [getTimetoken](index.md#1142058905%2FFunctions%2F126356644)(): [Long](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html) |
| [getUserMetadata](index.md#98903611%2FFunctions%2F126356644) | [jvm]<br>abstract fun [getUserMetadata](index.md#98903611%2FFunctions%2F126356644)(): JsonElement |
