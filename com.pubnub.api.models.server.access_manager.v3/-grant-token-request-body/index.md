[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.server.access_manager.v3](../index.md) / [GrantTokenRequestBody](./index.md)

# GrantTokenRequestBody

`data class GrantTokenRequestBody`

### Types

| Name | Summary |
|---|---|
| [GrantTokenPermission](-grant-token-permission/index.md) | `data class GrantTokenPermission` |
| [GrantTokenPermissions](-grant-token-permissions/index.md) | `data class GrantTokenPermissions` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GrantTokenRequestBody(ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, permissions: GrantTokenPermissions)` |

### Properties

| Name | Summary |
|---|---|
| [permissions](permissions.md) | `val permissions: GrantTokenPermissions` |
| [ttl](ttl.md) | `val ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [of](of.md) | `fun of(ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChannelGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md)`>, groups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChannelGroupGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-group-grant/index.md)`>, uuids: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`UUIDGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md)`>, meta: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`GrantTokenRequestBody`](./index.md) |
