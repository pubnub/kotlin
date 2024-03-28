//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[unsubscribe](unsubscribe.md)

# unsubscribe

[jvm]\
abstract fun [unsubscribe](unsubscribe.md)(): [UnsubscribeBuilder](../../com.pubnub.api.builder/-unsubscribe-builder/index.md)

When subscribed to a single channel, this function causes the client to issue a leave from the channel and close any open socket to the PubNub Network.

For multiplexed channels, the specified channel(s) will be removed and the socket remains open until there are no more channels remaining in the list.

**WARNING** Unsubscribing from all the channel(s) and then subscribing to a new channel Y isn't the same as Subscribing to channel Y and then unsubscribing from the previously subscribed channel(s).

Unsubscribing from all the channels resets the timetoken and thus, there could be some gaps in the subscriptions that may lead to a message loss.
