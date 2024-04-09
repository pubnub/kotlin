//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[maintainPresenceState](maintain-presence-state.md)

# maintainPresenceState

[jvm]\
open override var [maintainPresenceState](maintain-presence-state.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState](../-pub-nub/set-presence-state.md) for the current [userId](user-id.md) with every automatic heartbeat (if [heartbeatInterval](heartbeat-interval.md) is greater than 0) and initial subscribe connection (also after e.g. loss of network).

Defaults to `true`.

Please note that `maintainPresenceState` doesn't apply to state that was set on channel groups. It is recommended to disable this option if you set state for channel groups using [PubNub.setPresenceState](../-pub-nub/set-presence-state.md), otherwise that state may be overwritten by individual channel states.
