[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.message_actions](../index.md) / [GetMessageActions](./index.md)

# GetMessageActions

`class GetMessageActions : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`PNGetMessageActionsResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)`, `[`PNGetMessageActionsResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)`>`

**See Also**

[PubNub.getMessageActions](../../com.pubnub.api/-pub-nub/get-message-actions.md)

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | `val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [page](page.md) | `val page: `[`PNBoundedPage`](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`PNGetMessageActionsResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)`>): `[`PNGetMessageActionsResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`PNGetMessageActionsResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNGetMessageActions` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
