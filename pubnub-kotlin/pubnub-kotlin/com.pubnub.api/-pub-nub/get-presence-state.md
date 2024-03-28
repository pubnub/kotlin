//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getPresenceState](get-presence-state.md)

# getPresenceState

[jvm]\
abstract fun [getPresenceState](get-presence-state.md)(channels: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = listOf(), channelGroups: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = listOf(), uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = configuration.userId.value): [GetState](../../com.pubnub.api.endpoints.presence/-get-state/index.md)

Retrieve state information specific to a subscriber UUID.

State information is supplied as a JSON object of key/value pairs.

#### Parameters

jvm

| | |
|---|---|
| channels | Channels to get the state from. |
| channelGroups | Channel groups to get the state from. |
| uuid | UUID of the user to get the state from. Defaults to the UUID of the client.     @see [PNConfiguration.uuid](../-p-n-configuration/uuid.md) |
