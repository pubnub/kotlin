[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.message_actions](../index.md) / [GetMessageActions](./index.md)

# GetMessageActions

`class GetMessageActions : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`>>, `[`PNGetMessageActionsResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md)`>`

**See Also**

[PubNub.getMessageActions](../../com.pubnub.api/-pub-nub/get-message-actions.md)

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | `val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [end](end.md) | `val end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [limit](limit.md) | `val limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [start](start.md) | `val start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`>>>): `[`PNGetMessageActionsResult`](../../com.pubnub.api.models.consumer.message_actions/-p-n-get-message-actions-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`EntityEnvelope`](../../com.pubnub.api.models.server.objects_api/-entity-envelope/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`>>>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNGetMessageActions` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
