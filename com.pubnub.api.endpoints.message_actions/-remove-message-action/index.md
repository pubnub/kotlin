[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.message_actions](../index.md) / [RemoveMessageAction](./index.md)

# RemoveMessageAction

`class RemoveMessageAction : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNRemoveMessageActionResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)`>`

**See Also**

[PubNub.removeMessageAction](../../com.pubnub.api/-pub-nub/remove-message-action.md)

### Properties

| Name | Summary |
|---|---|
| [actionTimetoken](action-timetoken.md) | `val actionTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [channel](channel.md) | `val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [messageTimetoken](message-timetoken.md) | `val messageTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNRemoveMessageActionResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-remove-message-action-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): <ERROR CLASS>` |
| [operationType](operation-type.md) | `fun operationType(): PNDeleteMessageAction` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
