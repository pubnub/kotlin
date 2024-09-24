//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.models.consumer.access_manager.v3](../index.md)/[PNToken](index.md)

# PNToken

[common]\
data class [PNToken](index.md)(val version: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val timestamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, val ttl: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, val authorizedUUID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val resources: [PNToken.PNTokenResources](-p-n-token-resources/index.md), val patterns: [PNToken.PNTokenResources](-p-n-token-resources/index.md), val meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null)

## Constructors

| | |
|---|---|
| [PNToken](-p-n-token.md) | [common]<br>constructor(version: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, timestamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, ttl: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, authorizedUUID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, resources: [PNToken.PNTokenResources](-p-n-token-resources/index.md), patterns: [PNToken.PNTokenResources](-p-n-token-resources/index.md), meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [PNResourcePermissions](-p-n-resource-permissions/index.md) | [common]<br>data class [PNResourcePermissions](-p-n-resource-permissions/index.md)(val read: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val write: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val manage: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val delete: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val get: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val update: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val join: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |
| [PNTokenResources](-p-n-token-resources/index.md) | [common]<br>data class [PNTokenResources](-p-n-token-resources/index.md)(val channels: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNToken.PNResourcePermissions](-p-n-resource-permissions/index.md)&gt; = emptyMap(), val channelGroups: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNToken.PNResourcePermissions](-p-n-resource-permissions/index.md)&gt; = emptyMap(), val uuids: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [PNToken.PNResourcePermissions](-p-n-resource-permissions/index.md)&gt; = emptyMap()) |

## Properties

| Name | Summary |
|---|---|
| [authorizedUUID](authorized-u-u-i-d.md) | [common]<br>val [authorizedUUID](authorized-u-u-i-d.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null |
| [meta](meta.md) | [common]<br>val [meta](meta.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null |
| [patterns](patterns.md) | [common]<br>val [patterns](patterns.md): [PNToken.PNTokenResources](-p-n-token-resources/index.md) |
| [resources](resources.md) | [common]<br>val [resources](resources.md): [PNToken.PNTokenResources](-p-n-token-resources/index.md) |
| [timestamp](timestamp.md) | [common]<br>val [timestamp](timestamp.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0 |
| [ttl](ttl.md) | [common]<br>val [ttl](ttl.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0 |
| [version](version.md) | [common]<br>val [version](version.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
