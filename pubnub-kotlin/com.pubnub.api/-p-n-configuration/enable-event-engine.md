//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[enableEventEngine](enable-event-engine.md)

# enableEventEngine

[jvm]\
var [enableEventEngine](enable-event-engine.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true

This controls whether to enable the new implementation of Subscription and Presence handling.

The current default is `true`.

If you encounter errors with the setting enabled you can set it to `false` to go back to the previous behavior.

Please note that this setting will be removed in a future version of the SDK, so please report any errors that are preventing your from keeping it enabled.
