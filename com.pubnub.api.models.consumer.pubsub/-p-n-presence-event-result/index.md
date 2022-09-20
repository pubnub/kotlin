[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub](../index.md) / [PNPresenceEventResult](./index.md)

# PNPresenceEventResult

`data class PNPresenceEventResult : `[`PNEvent`](../-p-n-event.md)

Wrapper around a presence event received in [SubscribeCallback.presence](../../com.pubnub.api.callbacks/-subscribe-callback/presence.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Wrapper around a presence event received in [SubscribeCallback.presence](../../com.pubnub.api.callbacks/-subscribe-callback/presence.md).`PNPresenceEventResult(event: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, timestamp: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, occupancy: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, state: JsonElement? = null, channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, join: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, leave: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, timeout: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, hereNowRefresh: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, userMetadata: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | The channel which the `event` is performed on.`val channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [event](event.md) | The presence event. Could be `join`, `leave`, `state-change` or `interval`.`val event: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [hereNowRefresh](here-now-refresh.md) | Indicates to the user that a manual [PubNub.hereNow](../../com.pubnub.api/-pub-nub/here-now.md) should be called to get the complete list of users present in the channel.`val hereNowRefresh: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [join](join.md) | List of users that have *joined* the `channel` if the `event` is an `interval`. This needs to be enabled under **presence_deltas** at the Admin Dashboard.`val join: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [leave](leave.md) | List of users that have *left* the `channel` if the `event` is an `interval`. This needs to be enabled under **presence_deltas** at the Admin Dashboard.`val leave: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [occupancy](occupancy.md) | Total number of users currently present in the `channel` in question.`val occupancy: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [state](state.md) | Presence state of the related UUID, if any.`val state: JsonElement?` |
| [subscription](subscription.md) | The related subscription.`val subscription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [timeout](timeout.md) | List of users that have *timed out* of the `channel` if the `event` is an `interval`. This needs to be enabled under **presence_deltas** at the Admin Dashboard.`val timeout: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [timestamp](timestamp.md) | The timestamp of the event.`val timestamp: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [timetoken](timetoken.md) | The timetoken of the event.`val timetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [userMetadata](user-metadata.md) | User metadata if any.`val userMetadata: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [uuid](uuid.md) | The UUID which the presence event is related to.`val uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
