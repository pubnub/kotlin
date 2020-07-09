[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints](../index.md) / [FetchMessages](./index.md)

# FetchMessages

`class FetchMessages : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.md)`, `[`PNFetchMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)`>`

**See Also**

[PubNub.fetchMessages](../../com.pubnub.api/-pub-nub/fetch-messages.md)

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | Channels to return history messages from.`lateinit var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [end](end.md) | Time token delimiting the end of time slice (inclusive) to pull messages from.`var end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [includeMessageActions](include-message-actions.md) | Whether to include message actions in response.`var includeMessageActions: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeMeta](include-meta.md) | Whether to include message metadata in response.`var includeMeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [maximumPerChannel](maximum-per-channel.md) | Specifies the number of historical messages to return per channel. If [includeMessageActions](include-message-actions.md) is `false`, then `1` is the default (and maximum) value. Otherwise it's `25`.`var maximumPerChannel: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [start](start.md) | Timetoken delimiting the start of time slice (exclusive) to pull messages from.`var start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.md)`>): `[`PNFetchMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.md)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNFetchMessagesOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
