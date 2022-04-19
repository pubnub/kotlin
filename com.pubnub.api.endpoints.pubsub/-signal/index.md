[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.pubsub](../index.md) / [Signal](./index.md)

# Signal

`class Signal : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>, `[`PNPublishResult`](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md)`>`

**See Also**

[PubNub.signal](../../com.pubnub.api/-pub-nub/signal.md)

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | `val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [message](message.md) | `val message: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>): `[`PNPublishResult`](../../com.pubnub.api.models.consumer/-p-n-publish-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: <ERROR CLASS><`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): <ERROR CLASS>` |
| [isPubKeyRequired](is-pub-key-required.md) | `fun isPubKeyRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.md) | `fun operationType(): PNSignalOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
