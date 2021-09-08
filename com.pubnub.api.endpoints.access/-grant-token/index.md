[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.access](../index.md) / [GrantToken](./index.md)

# GrantToken

`class GrantToken : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`GrantTokenResponse`](../../com.pubnub.api.models.server.access_manager.v3/-grant-token-response/index.md)`, `[`PNGrantTokenResult`](../../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)`>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GrantToken(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, meta: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, authorizedUUID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChannelGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md)`>, channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChannelGroupGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-group-grant/index.md)`>, uuids: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`UUIDGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)`>)` |

### Properties

| Name | Summary |
|---|---|
| [ttl](ttl.md) | `val ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`GrantTokenResponse`](../../com.pubnub.api.models.server.access_manager.v3/-grant-token-response/index.md)`>): `[`PNGrantTokenResult`](../../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant-token-result/index.md)`?` |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`GrantTokenResponse`](../../com.pubnub.api.models.server.access_manager.v3/-grant-token-response/index.md)`>` |
| [getAffectedChannelGroups](get-affected-channel-groups.md) | `fun getAffectedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getAffectedChannels](get-affected-channels.md) | `fun getAffectedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [isAuthRequired](is-auth-required.md) | `fun isAuthRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
