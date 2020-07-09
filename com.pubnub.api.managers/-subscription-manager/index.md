[pubnub-kotlin](../../index.md) / [com.pubnub.api.managers](../index.md) / [SubscriptionManager](./index.md)

# SubscriptionManager

`class SubscriptionManager`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SubscriptionManager(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [pubnub](pubnub.md) | `val pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md) |

### Functions

| Name | Summary |
|---|---|
| [addListener](add-listener.md) | `fun addListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [destroy](destroy.md) | `fun destroy(forceDestroy: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [disconnect](disconnect.md) | `fun disconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getSubscribedChannelGroups](get-subscribed-channel-groups.md) | `fun getSubscribedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getSubscribedChannels](get-subscribed-channels.md) | `fun getSubscribedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [reconnect](reconnect.md) | `fun reconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeListener](remove-listener.md) | `fun removeListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [unsubscribeAll](unsubscribe-all.md) | `fun unsubscribeAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
