[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.objects.uuid](../index.md) / [GetUUIDMetadata](./index.md)

# GetUUIDMetadata

`class GetUUIDMetadata : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`PNUUIDMetadata`](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata/index.md)`>, `[`PNUUIDMetadataResult`](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata-result/index.md)`>`

**See Also**

[PubNub.getUUIDMetadata](../../com.pubnub.api/-pub-nub/get-u-u-i-d-metadata.md)

### Properties

| Name | Summary |
|---|---|
| [uuid](uuid.md) | `val uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`PNUUIDMetadata`](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata/index.md)`>>): `[`PNUUIDMetadataResult`](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`PNUUIDMetadata`](../../com.pubnub.api.models.consumer.objects.uuid/-p-n-u-u-i-d-metadata/index.md)`>>` |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
