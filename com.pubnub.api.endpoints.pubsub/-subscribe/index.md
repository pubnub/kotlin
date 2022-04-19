[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.pubsub](../index.md) / [Subscribe](./index.md)

# Subscribe

`class Subscribe : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.md)`, `[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.md)`>`

### Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | `var channelGroups: <ERROR CLASS>` |
| [channels](channels.md) | `var channels: <ERROR CLASS>` |
| [filterExpression](filter-expression.md) | `var filterExpression: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [region](region.md) | `var region: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [state](state.md) | `var state: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [timetoken](timetoken.md) | `var timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.md)`>): `[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`SubscribeEnvelope`](../../com.pubnub.api.models.server/-subscribe-envelope/index.md)`>` |
| [getAffectedChannelGroups](get-affected-channel-groups.md) | `fun getAffectedChannelGroups(): <ERROR CLASS>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): <ERROR CLASS>` |
| [operationType](operation-type.md) | `fun operationType(): PNSubscribeOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
