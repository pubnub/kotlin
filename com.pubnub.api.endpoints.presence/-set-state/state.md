[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.presence](../index.md) / [SetState](index.md) / [state](./state.md)

# state

`lateinit var state: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)

The actual state object to set.

NOTE: Presence state must be expressed as a JsonObject.
When calling [PubNub.setPresenceState](../../com.pubnub.api/-pub-nub/set-presence-state.md), be sure to supply an initialized JsonObject
or POJO which can be serialized to a JsonObject.

