//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionOptions](index.md)/[plus](plus.md)

# plus

[common]\
open operator fun [plus](plus.md)(options: [SubscriptionOptions](index.md)): [SubscriptionOptions](index.md)

Combine multiple options, for example:

val options = `SubscriptionOptions.filter { /* some expression*/ } + SubscriptionOptions.receivePresenceEvents()`
