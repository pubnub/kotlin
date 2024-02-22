//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscribeCapable](index.md)/[unsubscribe](unsubscribe.md)

# unsubscribe

[jvm]\
abstract fun [unsubscribe](unsubscribe.md)()

Stop receiving events from this subscription.

Please note that if there are any other subscriptions to the same channel or channel group, they will continue to receive events until they are also unsubscribed. Only once there are no longer any active subscriptions to a given channel/group, the [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/index.md) client will remove that channel/group from the connection.
