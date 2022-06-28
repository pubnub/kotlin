[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.access_manager.sum](../index.md) / [UserPermissions](./index.md)

# UserPermissions

`interface UserPermissions : `[`PNGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-p-n-grant/index.md)

### Companion Object Functions

| Name | Summary |
|---|---|
| [id](id.md) | `fun id(userId: `[`UserId`](../../com.pubnub.api/-user-id/index.md)`, get: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, update: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`UserPermissions`](./index.md) |
| [pattern](pattern.md) | `fun pattern(pattern: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, get: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, update: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`UserPermissions`](./index.md) |

### Extension Functions

| Name | Summary |
|---|---|
| [toUuidGrant](../to-uuid-grant.md) | `fun `[`UserPermissions`](./index.md)`.toUuidGrant(): `[`UUIDGrant`](../../com.pubnub.api.models.consumer.access_manager.v3/-u-u-i-d-grant/index.md) |
