//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionOptions](index.md)/[plus](plus.md)

# plus

[jvm]\
open operator fun [plus](plus.md)(options: [SubscriptionOptions](index.md)): [SubscriptionOptions](index.md)

Combine multiple options, for example:

val options = `SubscriptionOptions.filter { /* some expression*/ } + SubscriptionOptions.receivePresenceEvents()`
