//[pubnub-kotlin](../../index.md)/[com.pubnub.api.models.server.access_manager.v3](index.md)

# Package com.pubnub.api.models.server.access_manager.v3

## Types

| Name | Summary |
|---|---|
| [GrantTokenData](-grant-token-data/index.md) | [jvm]<br>data class [GrantTokenData](-grant-token-data/index.md)(val token: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [GrantTokenRequestBody](-grant-token-request-body/index.md) | [jvm]<br>data class [GrantTokenRequestBody](-grant-token-request-body/index.md)(val ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val permissions: [GrantTokenRequestBody.GrantTokenPermissions](-grant-token-request-body/-grant-token-permissions/index.md)) |
| [GrantTokenResponse](-grant-token-response/index.md) | [jvm]<br>data class [GrantTokenResponse](-grant-token-response/index.md)(val data: [GrantTokenData](-grant-token-data/index.md)) |
| [RevokeTokenData](-revoke-token-data/index.md) | [jvm]<br>data class [RevokeTokenData](-revoke-token-data/index.md)(val message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val token: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [RevokeTokenResponse](-revoke-token-response/index.md) | [jvm]<br>data class [RevokeTokenResponse](-revoke-token-response/index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [RevokeTokenData](-revoke-token-data/index.md), val service: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
