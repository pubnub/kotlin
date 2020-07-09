[pubnub-kotlin](../../index.md) / [com.pubnub.api.builder](../index.md) / [PubSubBuilder](./index.md)

# PubSubBuilder

`abstract class PubSubBuilder`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PubSubBuilder(subscriptionManager: `[`SubscriptionManager`](../../com.pubnub.api.managers/-subscription-manager/index.md)`, channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList())` |

### Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channel](#) are required.`var channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channels](channels.md) | Channels to subscribe/unsubscribe. Either `channel` or [channelGroups](channel-groups.md) are required.`var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [subscriptionManager](subscription-manager.md) | `val subscriptionManager: `[`SubscriptionManager`](../../com.pubnub.api.managers/-subscription-manager/index.md) |

### Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | `abstract fun execute(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [PresenceBuilder](../-presence-builder/index.md) | `class PresenceBuilder : `[`PubSubBuilder`](./index.md) |
| [SubscribeBuilder](../-subscribe-builder/index.md) | `class SubscribeBuilder : `[`PubSubBuilder`](./index.md) |
| [UnsubscribeBuilder](../-unsubscribe-builder/index.md) | `class UnsubscribeBuilder : `[`PubSubBuilder`](./index.md) |
