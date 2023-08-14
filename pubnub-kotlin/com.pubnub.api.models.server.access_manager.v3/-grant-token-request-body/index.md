//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.server.access_manager.v3](../index.md)/[GrantTokenRequestBody](index.md)

# GrantTokenRequestBody

[jvm]\
data class [GrantTokenRequestBody](index.md)(val ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val permissions: [GrantTokenRequestBody.GrantTokenPermissions](-grant-token-permissions/index.md))

## Constructors

| | |
|---|---|
| [GrantTokenRequestBody](-grant-token-request-body.md) | [jvm]<br>fun [GrantTokenRequestBody](-grant-token-request-body.md)(ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), permissions: [GrantTokenRequestBody.GrantTokenPermissions](-grant-token-permissions/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [GrantTokenPermission](-grant-token-permission/index.md) | [jvm]<br>data class [GrantTokenPermission](-grant-token-permission/index.md)(val channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; = emptyMap(), val groups: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; = emptyMap(), val uuids: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; = emptyMap(), val spaces: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; = emptyMap(), val users: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; = emptyMap()) |
| [GrantTokenPermissions](-grant-token-permissions/index.md) | [jvm]<br>data class [GrantTokenPermissions](-grant-token-permissions/index.md)(val resources: [GrantTokenRequestBody.GrantTokenPermission](-grant-token-permission/index.md), val patterns: [GrantTokenRequestBody.GrantTokenPermission](-grant-token-permission/index.md), val meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [permissions](permissions.md) | [jvm]<br>val [permissions](permissions.md): [GrantTokenRequestBody.GrantTokenPermissions](-grant-token-permissions/index.md) |
| [ttl](ttl.md) | [jvm]<br>val [ttl](ttl.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
