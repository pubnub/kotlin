[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints](../index.md) / [History](./index.md)

# History

`class History : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<JsonElement, `[`PNHistoryResult`](../../com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)`>`

**See Also**

[PubNub.history](../../com.pubnub.api/-pub-nub/history.md)

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | Channel to return history messages from.`lateinit var channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [count](count.md) | Specifies the number of historical messages to return. Default and maximum value is `100`.`var count: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [end](end.md) | Timetoken delimiting the end of time slice (inclusive) to pull messages from.`var end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [includeMeta](include-meta.md) | Whether to include message metadata in response.`var includeMeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeTimetoken](include-timetoken.md) | Whether to include message timetokens in the response.`var includeTimetoken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [reverse](reverse.md) | Whether to traverse the time ine in reverse starting with the oldest message first.`var reverse: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [start](start.md) | Timetoken delimiting the start of time slice (exclusive) to pull messages from.`var start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<JsonElement>): `[`PNHistoryResult`](../../com.pubnub.api.models.consumer.history/-p-n-history-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<JsonElement>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNHistoryOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
