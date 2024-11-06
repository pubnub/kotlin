//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer.pubsub](../index.md)/[PNPresenceEventResult](index.md)

# PNPresenceEventResult

[common]\
class [PNPresenceEventResult](index.md)(val event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val timestamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val occupancy: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, val state: [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null, val channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val join: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, val leave: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, val timeout: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, val hereNowRefresh: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, val userMetadata: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null) : [PNEvent](../-p-n-event/index.md)

Wrapper around a presence event.

## Constructors

| | |
|---|---|
| [PNPresenceEventResult](-p-n-presence-event-result.md) | [common]<br>constructor(event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, timestamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, occupancy: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, state: [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null, channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), subscription: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, join: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, leave: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, timeout: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, hereNowRefresh: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, userMetadata: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [common]<br>open override val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The channel which the `event` is performed on. |
| [event](event.md) | [common]<br>val [event](event.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The presence event. Could be `join`, `leave`, `state-change` or `interval`. |
| [hereNowRefresh](here-now-refresh.md) | [common]<br>val [hereNowRefresh](here-now-refresh.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null<br>Indicates to the user that a manual HereNow should be called to get the complete list of users present in the channel. |
| [join](join.md) | [common]<br>val [join](join.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null<br>List of users that have *joined* the `channel` if the `event` is an `interval`. This needs to be enabled under **presence_deltas** at the Admin Dashboard. |
| [leave](leave.md) | [common]<br>val [leave](leave.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null<br>List of users that have *left* the `channel` if the `event` is an `interval`. This needs to be enabled under **presence_deltas** at the Admin Dashboard. |
| [occupancy](occupancy.md) | [common]<br>val [occupancy](occupancy.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null<br>Total number of users currently present in the `channel` in question. |
| [state](state.md) | [common]<br>val [state](state.md): [JsonElement](../../com.pubnub.api/-json-element/index.md)? = null<br>Presence state of the related UUID, if any. |
| [subscription](subscription.md) | [common]<br>open override val [subscription](subscription.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The related subscriptions. |
| [timeout](timeout.md) | [common]<br>val [timeout](timeout.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null<br>List of users that have *timed out* of the `channel` if the `event` is an `interval`. This needs to be enabled under **presence_deltas** at the Admin Dashboard. |
| [timestamp](timestamp.md) | [common]<br>val [timestamp](timestamp.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null<br>The timestamp of the event. |
| [timetoken](timetoken.md) | [common]<br>open override val [timetoken](timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null<br>The timetoken of the event. |
| [userMetadata](user-metadata.md) | [common]<br>val [userMetadata](user-metadata.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null<br>User metadata if any. |
| [uuid](uuid.md) | [common]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>The UUID which the presence event is related to. |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
