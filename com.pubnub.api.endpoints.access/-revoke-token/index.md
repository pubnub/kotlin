[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.access](../index.md) / [RevokeToken](./index.md)

# RevokeToken

`class RevokeToken : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`RevokeTokenResponse`](../../com.pubnub.api.models.server.access_manager.v3/-revoke-token-response/index.md)`, `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RevokeToken(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, token: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`RevokeTokenResponse`](../../com.pubnub.api.models.server.access_manager.v3/-revoke-token-response/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [doWork](do-work.md) | `fun doWork(queryParams: <ERROR CLASS><`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`RevokeTokenResponse`](../../com.pubnub.api.models.server.access_manager.v3/-revoke-token-response/index.md)`>` |
| [isAuthRequired](is-auth-required.md) | `fun isAuthRequired(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [operationType](operation-type.md) | `fun operationType(): `[`PNOperationType`](../../com.pubnub.api.enums/-p-n-operation-type/index.md) |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
