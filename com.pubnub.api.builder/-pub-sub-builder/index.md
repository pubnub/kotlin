---
title: PubSubBuilder - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.builder](../index.html) / [PubSubBuilder](./index.html)

# PubSubBuilder

`abstract class PubSubBuilder`

### Constructors

| [&lt;init&gt;](-init-.html) | `PubSubBuilder(subscriptionManager: `[`SubscriptionManager`](../../com.pubnub.api.managers/-subscription-manager/index.html)`, channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList())` |

### Properties

| [channelGroups](channel-groups.html) | `var channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [channels](channels.html) | `var channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [subscriptionManager](subscription-manager.html) | `val subscriptionManager: `[`SubscriptionManager`](../../com.pubnub.api.managers/-subscription-manager/index.html) |

### Functions

| [execute](execute.html) | `abstract fun execute(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| [PresenceBuilder](../-presence-builder/index.html) | `class PresenceBuilder : `[`PubSubBuilder`](./index.html) |
| [SubscribeBuilder](../-subscribe-builder/index.html) | `class SubscribeBuilder : `[`PubSubBuilder`](./index.html) |
| [UnsubscribeBuilder](../-unsubscribe-builder/index.html) | `class UnsubscribeBuilder : `[`PubSubBuilder`](./index.html) |

