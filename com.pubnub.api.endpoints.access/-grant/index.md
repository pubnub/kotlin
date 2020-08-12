[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.access](../index.md) / [Grant](./index.md)

# Grant

`class Grant : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`AccessManagerGrantPayload`](../../com.pubnub.api.models.server.access_manager/-access-manager-grant-payload/index.md)`>, `[`PNAccessManagerGrantResult`](../../com.pubnub.api.models.consumer.access_manager/-p-n-access-manager-grant-result/index.md)`>`

**See Also**

[PubNub.grant](../../com.pubnub.api/-pub-nub/grant.md)

### Properties

| Name | Summary |
|---|---|
| [authKeys](auth-keys.md) | `val authKeys: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channelGroups](channel-groups.md) | `val channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channels](channels.md) | `val channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [delete](delete.md) | `val delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [manage](manage.md) | `val manage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [read](read.md) | `val read: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [ttl](ttl.md) | `val ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [write](write.md) | `val write: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`AccessManagerGrantPayload`](../../com.pubnub.api.models.server.access_manager/-access-manager-grant-payload/index.md)`>>): `[`PNAccessManagerGrantResult`](../../com.pubnub.api.models.consumer.access_manager/-p-n-access-manager-grant-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Envelope`](../../com.pubnub.api.models.server/-envelope/index.md)`<`[`AccessManagerGrantPayload`](../../com.pubnub.api.models.server.access_manager/-access-manager-grant-payload/index.md)`>>` |
| [getAffectedChannelGroups](get-affected-channel-groups.md) | `fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [isAuthRequired](is-auth-required.md) | `fun isAuthRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.md) | `fun operationType(): PNAccessManagerGrant` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
