[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints](../index.md) / [History](./index.md)

# History

`class History : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<JsonElement, `[`PNHistoryResult`](../../com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)`>`

**See Also**

[PubNub.history](../../com.pubnub.api/-pub-nub/history.md)

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | `val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [count](count.md) | `val count: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [end](end.md) | `val end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [includeMeta](include-meta.md) | `val includeMeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeTimetoken](include-timetoken.md) | `val includeTimetoken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [reverse](reverse.md) | `val reverse: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [start](start.md) | `val start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<JsonElement>): `[`PNHistoryResult`](../../com.pubnub.api.models.consumer.history/-p-n-history-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<JsonElement>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNHistoryOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
