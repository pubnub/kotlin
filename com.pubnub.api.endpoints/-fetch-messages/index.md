[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints](../index.md) / [FetchMessages](./index.md)

# FetchMessages

`class FetchMessages : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.md)`, `[`PNFetchMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md)`>`

**See Also**

[PubNub.fetchMessages](../../com.pubnub.api/-pub-nub/fetch-messages.md)

### Properties

| Name | Summary |
|---|---|
| [channels](channels.md) | `val channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [includeMessageActions](include-message-actions.md) | `val includeMessageActions: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeMeta](include-meta.md) | `val includeMeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeUUID](include-u-u-i-d.md) | `val includeUUID: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [page](page.md) | `val page: `[`PNBoundedPage`](../../com.pubnub.api.models.consumer/-p-n-bounded-page/index.md) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.md)`>): `[`PNFetchMessagesResult`](../../com.pubnub.api.models.consumer.history/-p-n-fetch-messages-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`FetchMessagesEnvelope`](../../com.pubnub.api.models.server/-fetch-messages-envelope/index.md)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNFetchMessagesOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
