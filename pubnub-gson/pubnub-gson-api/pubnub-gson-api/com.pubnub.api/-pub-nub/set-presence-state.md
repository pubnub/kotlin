//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[setPresenceState](set-presence-state.md)

# setPresenceState

[jvm]\
abstract fun [setPresenceState](set-presence-state.md)(): [SetState](../../com.pubnub.api.endpoints.presence/-set-state/index.md)

Set state information specific to a subscriber UUID.

State information is supplied as a JSON object of key/value pairs.

If [PNConfiguration.setMaintainPresenceState](../-p-n-configuration/set-maintain-presence-state.md) is `true`, and the `uuid` matches PNConfiguration.getUuid, the state for channels will be saved in the PubNub client and resent with every heartbeat and initial subscribe request. In that case, it's not recommended to mix setting state through channels *and* channel groups, as state set through the channel group will be overwritten after the next heartbeat or subscribe reconnection (e.g. after loss of network).
