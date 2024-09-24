//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscribeCapable](index.md)/[subscribe](subscribe.md)

# subscribe

[common]\
abstract fun [subscribe](subscribe.md)(cursor: [SubscriptionCursor](../-subscription-cursor/index.md) = SubscriptionCursor(0))

Start receiving events from the subscription (or subscriptions) represented by this object.

The PubNub client will start a network connection to the server if it doesn't have one already, or will alter the existing connection to add channels and groups requested by this subscription if needed.

Please note that passing a [cursor](subscribe.md) to [subscribe](subscribe.md) affects *all* subscriptions that are currently active in the [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/index.md) client, as it will reset the global timetoken for the server connection. If an active subscription had previously delivered *any* events to its listeners, it will only deliver events *newer* that the last timetoken it recorded.
