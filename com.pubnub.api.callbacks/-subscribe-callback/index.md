---
title: SubscribeCallback - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.callbacks](../index.html) / [SubscribeCallback](./index.html)

# SubscribeCallback

`abstract class SubscribeCallback`

### Constructors

| [&lt;init&gt;](-init-.html) | `SubscribeCallback()` |

### Functions

| [message](message.html) | `open fun message(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`, pnMessageResult: `[`PNMessageResult`](../../com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [messageAction](message-action.html) | `open fun messageAction(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`, pnMessageActionResult: `[`PNMessageActionResult`](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [presence](presence.html) | `open fun presence(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`, pnPresenceEventResult: `[`PNPresenceEventResult`](../../com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [signal](signal.html) | `open fun signal(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`, pnSignalResult: `[`PNSignalResult`](../../com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [status](status.html) | `abstract fun status(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.html)`, pnStatus: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

