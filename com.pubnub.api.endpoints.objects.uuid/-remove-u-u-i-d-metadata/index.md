[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.objects.uuid](../index.md) / [RemoveUUIDMetadata](./index.md)

# RemoveUUIDMetadata

`class RemoveUUIDMetadata : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>, `[`PNRemoveMetadataResult`](../../com.pubnub.api.models.consumer.objects/-p-n-remove-metadata-result/index.md)`>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RemoveUUIDMetadata(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [uuid](uuid.md) | `val uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>>): `[`PNRemoveMetadataResult`](../../com.pubnub.api.models.consumer.objects/-p-n-remove-metadata-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>>` |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
