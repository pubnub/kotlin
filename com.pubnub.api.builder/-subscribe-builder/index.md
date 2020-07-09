[pubnub-kotlin](../../index.md) / [com.pubnub.api.builder](../index.md) / [SubscribeBuilder](./index.md)

# SubscribeBuilder

`class SubscribeBuilder : `[`PubSubBuilder`](../-pub-sub-builder/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SubscribeBuilder(subscriptionManager: `[`SubscriptionManager`](../../com.pubnub.api.managers/-subscription-manager/index.md)`, withPresence: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, withTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0L)` |

### Properties

| Name | Summary |
|---|---|
| [withPresence](with-presence.md) | Also subscribe to related presence channel.`var withPresence: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [withTimetoken](with-timetoken.md) | A timetoken to start the subscribe loop from.`var withTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

### Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | `fun execute(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
