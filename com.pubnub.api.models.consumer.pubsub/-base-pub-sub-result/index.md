---
title: BasePubSubResult - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.models.consumer.pubsub](../index.html) / [BasePubSubResult](./index.html)

# BasePubSubResult

`open class BasePubSubResult`

### Constructors

| [&lt;init&gt;](-init-.html) | `BasePubSubResult(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?, userMetadata: JsonElement?, publisher: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| [channel](channel.html) | `val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [publisher](publisher.html) | `val publisher: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [subscription](subscription.html) | `val subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [timetoken](timetoken.html) | `val timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [userMetadata](user-metadata.html) | `val userMetadata: JsonElement?` |

### Inheritors

| [MessageResult](../-message-result/index.html) | `open class MessageResult : `[`BasePubSubResult`](./index.html) |
| [ObjectResult](../../com.pubnub.api.models.consumer.pubsub.objects/-object-result/index.html) | `open class ObjectResult<T> : `[`BasePubSubResult`](./index.html) |

