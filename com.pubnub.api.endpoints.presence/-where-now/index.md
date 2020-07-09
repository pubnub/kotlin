[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.presence](../index.md) / [WhereNow](./index.md)

# WhereNow

`class WhereNow : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`WhereNowPayload`](../../com.pubnub.api.models.server.presence/-where-now-payload/index.md)`>, `[`PNWhereNowResult`](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)`>`

**See Also**

[PubNub.whereNow](../../com.pubnub.api/-pub-nub/where-now.md)

### Properties

| Name | Summary |
|---|---|
| [uuid](uuid.md) | UUID of the user to get its current channel subscriptions.`var uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`WhereNowPayload`](../../com.pubnub.api.models.server.presence/-where-now-payload/index.md)`>>): `[`PNWhereNowResult`](../../com.pubnub.api.models.consumer.presence/-p-n-where-now-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`WhereNowPayload`](../../com.pubnub.api.models.server.presence/-where-now-payload/index.md)`>>` |
| [operationType](operation-type.md) | `fun operationType(): PNWhereNowOperation` |
