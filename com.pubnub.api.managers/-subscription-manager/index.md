---
title: SubscriptionManager - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.managers](../index.html) / [SubscriptionManager](./index.html)

# SubscriptionManager

`class SubscriptionManager`

### Constructors

| [&lt;init&gt;](-init-.html) | `SubscriptionManager(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`)` |

### Properties

| [pubnub](pubnub.html) | `val pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html) |

### Functions

| [addListener](add-listener.html) | `fun addListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [destroy](destroy.html) | `fun destroy(forceDestroy: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [disconnect](disconnect.html) | `fun disconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getSubscribedChannelGroups](get-subscribed-channel-groups.html) | `fun getSubscribedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getSubscribedChannels](get-subscribed-channels.html) | `fun getSubscribedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [reconnect](reconnect.html) | `fun reconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeListener](remove-listener.html) | `fun removeListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [unsubscribeAll](unsubscribe-all.html) | `fun unsubscribeAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

