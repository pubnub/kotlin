//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[Subscription](index.md)/[subscribe](subscribe.md)

# subscribe

[jvm]\
abstract fun [subscribe](subscribe.md)()

Start receiving events from the subscriptions (or subscriptions) represented by this object.

The PubNub client will start a network connection to the server if it doesn't have one already, or will alter the existing connection to add channels and groups requested by this subscriptions if needed.
