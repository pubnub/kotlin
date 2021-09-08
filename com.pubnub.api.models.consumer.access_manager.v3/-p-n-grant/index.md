[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.access_manager.v3](../index.md) / [PNGrant](./index.md)

# PNGrant

`interface PNGrant`

### Properties

| Name | Summary |
|---|---|
| [create](create.md) | `abstract val create: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [delete](delete.md) | `abstract val delete: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [get](get.md) | `abstract val get: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [id](id.md) | `abstract val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [join](join.md) | `abstract val join: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [manage](manage.md) | `abstract val manage: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [read](read.md) | `abstract val read: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [update](update.md) | `abstract val update: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [write](write.md) | `abstract val write: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [ChannelGrant](../-channel-grant/index.md) | `interface ChannelGrant : `[`PNGrant`](./index.md) |
| [ChannelGroupGrant](../-channel-group-grant/index.md) | `interface ChannelGroupGrant : `[`PNGrant`](./index.md) |
| [UUIDGrant](../-u-u-i-d-grant/index.md) | `interface UUIDGrant : `[`PNGrant`](./index.md) |
