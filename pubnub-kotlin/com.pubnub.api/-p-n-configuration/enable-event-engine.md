//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[enableEventEngine](enable-event-engine.md)

# enableEventEngine

[jvm]\
var [enableEventEngine](enable-event-engine.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false

This controls whether to enable a new, experimental implementation of Subscription and Presence handling.

The current default is `false`.

This switch can help you verify the behavior of the PubNub SDK with the new engine enabled in your app, however the change should be transparent for users.

It will default to true in a future SDK release.
