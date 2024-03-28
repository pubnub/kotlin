//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[setPresenceState](set-presence-state.md)

# setPresenceState

[jvm]\
abstract fun [setPresenceState](set-presence-state.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = listOf(), channelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = listOf(), state: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = configuration.userId.value): [SetState](../../com.pubnub.api.endpoints.presence/-set-state/index.md)

Set state information specific to a subscriber UUID.

State information is supplied as a JSON object of key/value pairs.

If [PNConfiguration.maintainPresenceState](../-p-n-configuration/maintain-presence-state.md) is `true`, and the `uuid` matches [PNConfiguration.uuid](../-p-n-configuration/uuid.md), the state for channels will be saved in the PubNub client and resent with every heartbeat and initial subscribe request. In that case, it's not recommended to mix setting state through channels *and* channel groups, as state set through the channel group will be overwritten after the next heartbeat or subscribe reconnection (e.g. after loss of network).

#### Parameters

jvm

| | |
|---|---|
| channels | Channels to set the state to. |
| channelGroups | Channel groups to set the state to. |
| state | The actual state object to set.     NOTE: Presence state must be expressed as a JsonObject.     When calling [PubNub.setPresenceState](set-presence-state.md), be sure to supply an initialized JsonObject     or POJO which can be serialized to a JsonObject. |
| uuid | UUID of the user to set the state for. Defaults to the UUID of the client.     @see [PNConfiguration.uuid](../-p-n-configuration/uuid.md) |
