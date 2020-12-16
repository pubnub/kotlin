[pubnub-kotlin](../../index.md) / [com.pubnub.api.callbacks](../index.md) / [SubscribeCallback](./index.md)

# SubscribeCallback

`abstract class SubscribeCallback`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SubscribeCallback()` |

### Functions

| Name | Summary |
|---|---|
| [file](file.md) | `open fun file(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnFileEventResult: `[`PNFileEventResult`](../../com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [message](message.md) | Receive messages at subscribed channels.`open fun message(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnMessageResult: `[`PNMessageResult`](../../com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [messageAction](message-action.md) | Receive message actions for messages in subscribed channels.`open fun messageAction(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnMessageActionResult: `[`PNMessageActionResult`](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [objects](objects.md) | `open fun objects(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, objectEvent: `[`PNObjectEventResult`](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [presence](presence.md) | Receive presence events for channels subscribed to with presence enabled via `withPresence = true` in [PubNub.subscribe](../../com.pubnub.api/-pub-nub/subscribe.md)`open fun presence(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnPresenceEventResult: `[`PNPresenceEventResult`](../../com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [signal](signal.md) | Receive signals at subscribed channels.`open fun signal(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnSignalResult: `[`PNSignalResult`](../../com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [status](status.md) | Receive status events like [PNStatusCategory.PNAcknowledgmentCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-acknowledgment-category.md), [PNStatusCategory.PNConnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-connected-category.md), [PNStatusCategory.PNReconnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-reconnected-category.md)`abstract fun status(pubnub: `[`PubNub`](../../com.pubnub.api/-pub-nub/index.md)`, pnStatus: `[`PNStatus`](../../com.pubnub.api.models.consumer/-p-n-status/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
