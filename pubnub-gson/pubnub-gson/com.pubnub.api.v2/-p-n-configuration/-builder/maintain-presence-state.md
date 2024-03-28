//[pubnub-gson](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[maintainPresenceState](maintain-presence-state.md)

# maintainPresenceState

[jvm]\
abstract fun [maintainPresenceState](maintain-presence-state.md)(maintainPresenceState: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration.Builder](index.md)

When `true` the SDK will resend the last channel state that was set using PubNub.setPresenceState for the current [userId](../../../../../pubnub-gson/com.pubnub.api.v2/-p-n-configuration/-builder/user-id.md) with every automatic heartbeat (if [heartbeatInterval](heartbeat-interval.md) is greater than 0) and initial subscribe connection (also after e.g. loss of network).

Applies only when enableEventEngine is true.

Defaults to `true`.

Please note that `maintainPresenceState` doesn't apply to state that was set on channel groups. It is recommended to disable this option if you set state for channel groups using PubNub.setPresenceState otherwise that state may be overwritten by individual channel states.
