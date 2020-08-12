[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.objects.channel](../index.md) / [GetAllChannelMetadata](./index.md)

# GetAllChannelMetadata

`class GetAllChannelMetadata : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNChannelMetadata`](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md)`>, `[`PNChannelMetadataArrayResult`](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata-array-result/index.md)`>`

**See Also**

[PubNub.getAllChannelMetadata](#)

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNChannelMetadata`](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md)`>>): `[`PNChannelMetadataArrayResult`](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata-array-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityArrayEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-array-envelope/index.md)`<`[`PNChannelMetadata`](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md)`>>` |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
