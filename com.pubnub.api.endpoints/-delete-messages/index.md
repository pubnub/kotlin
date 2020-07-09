[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints](../index.md) / [DeleteMessages](./index.md)

# DeleteMessages

`class DeleteMessages : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNDeleteMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-delete-messages-result/index.md)`>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeleteMessages(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | `lateinit var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [end](end.md) | `var end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [start](start.md) | `var start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNDeleteMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-delete-messages-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNDeleteMessagesOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
