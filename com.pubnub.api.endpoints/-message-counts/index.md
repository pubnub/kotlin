[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints](../index.md) / [MessageCounts](./index.md)

# MessageCounts

`class MessageCounts : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<JsonElement, `[`PNMessageCountResult`](../../com.pubnub.api.models.consumer.history/-p-n-message-count-result/index.md)`>`

**See Also**

[PubNub.messageCounts](../../com.pubnub.api/-pub-nub/message-counts.md)

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | `val channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channelsTimetoken](channels-timetoken.md) | `val channelsTimetoken: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<JsonElement>): `[`PNMessageCountResult`](../../com.pubnub.api.models.consumer.history/-p-n-message-count-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<JsonElement>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNMessageCountOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
