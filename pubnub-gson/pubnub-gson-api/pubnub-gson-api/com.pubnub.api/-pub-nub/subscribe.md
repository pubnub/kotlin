//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[subscribe](subscribe.md)

# subscribe

[jvm]\
abstract fun [subscribe](subscribe.md)(): [SubscribeBuilder](../../com.pubnub.api.builder/-subscribe-builder/index.md)

Causes the client to create an open TCP socket to the PubNub Real-Time Network and begin listening for messages on a specified channel.

To subscribe to a channel the client must send the appropriate [PNConfiguration.setSubscribeKey](../-p-n-configuration/set-subscribe-key.md) at initialization.

By default, a newly subscribed client will only receive messages published to the channel after the `subscribe()` call completes.

If a client gets disconnected from a channel, it can automatically attempt to reconnect to that channel and retrieve any available messages that were missed during that period. This can be achieved by setting [PNConfiguration.setRetryConfiguration](../-p-n-configuration/set-retry-configuration.md) when initializing the client.
