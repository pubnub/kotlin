[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.files](../index.md) / [PublishFileMessage](./index.md)

# PublishFileMessage

`open class PublishFileMessage : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>, `[`PNPublishFileMessageResult`](../../com.pubnub.api.models.consumer.files/-p-n-publish-file-message-result/index.md)`>`

**See Also**

[PubNub.publishFileMessage](../../com.pubnub.api/-pub-nub/publish-file-message.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PublishFileMessage(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, fileName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, fileId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, message: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, meta: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, shouldStore: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, pubNub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`)` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `open fun createResponse(input: Response<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>): `[`PNPublishFileMessageResult`](../../com.pubnub.api.models.consumer.files/-p-n-publish-file-message-result/index.md) |
| [doWork](do-work.md) | `open fun doWork(queryParams: <ERROR CLASS><`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [getAffectedChannelGroups](get-affected-channel-groups.md) | `open fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `open fun getAffectedChannels(): <ERROR CLASS>` |
| [isAuthRequired](is-auth-required.md) | `open fun isAuthRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isPubKeyRequired](is-pub-key-required.md) | `open fun isPubKeyRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isSubKeyRequired](is-sub-key-required.md) | `open fun isSubKeyRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.md) | `open fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [validateParams](validate-params.md) | `open fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
