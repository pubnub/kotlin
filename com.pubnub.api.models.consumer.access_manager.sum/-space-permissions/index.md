[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.access_manager.sum](../index.md) / [SpacePermissions](./index.md)

# SpacePermissions

`interface SpacePermissions : `[`PNGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant/index.md)

### Companion Object Functions

| Name | Summary |
|---|---|
| [id](id.md) | `fun id(spaceId: `[`SpaceId`](../../com.pubnub.api/-space-id/index.md)`, read: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, write: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, manage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, create: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, get: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, join: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, update: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`SpacePermissions`](./index.md) |
| [pattern](pattern.md) | `fun pattern(pattern: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, read: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, write: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, manage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, create: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, get: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, join: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, update: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`SpacePermissions`](./index.md) |

### Extension Functions

| Name | Summary |
|---|---|
| [toChannelGrant](../to-channel-grant.md) | `fun `[`SpacePermissions`](./index.md)`.toChannelGrant(): `[`ChannelGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-channel-grant/index.md) |
