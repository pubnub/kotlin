[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.access_manager](../index.md) / [PNAccessManagerGrantResult](./index.md)

# PNAccessManagerGrantResult

`class PNAccessManagerGrantResult`

Result of the [PubNub.grant](../../com.pubnub.api/-pub-nub/grant.md) operation

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Result of the [PubNub.grant](../../com.pubnub.api/-pub-nub/grant.md) operation`PNAccessManagerGrantResult(level: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, subscribeKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channels: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`PNAccessManagerKeyData`](../-p-n-access-manager-key-data/index.md)`>?>, channelGroups: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`PNAccessManagerKeyData`](../-p-n-access-manager-key-data/index.md)`>?>)` |

### Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | Access rights per channel group.`val channelGroups: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`PNAccessManagerKeyData`](../-p-n-access-manager-key-data/index.md)`>?>` |
| [channels](channels.md) | Access rights per channel.`val channels: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`PNAccessManagerKeyData`](../-p-n-access-manager-key-data/index.md)`>?>` |
| [level](level.md) | Permissions level, one of `subkey`, `subkey+auth`, `channel`, `channel-group`, `channel-group+auth` level.`val level: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeKey](subscribe-key.md) | The subscribe key.`val subscribeKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ttl](ttl.md) | Time in minutes for which granted permissions are valid. Value of `0` means indefinite.`val ttl: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
