[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [signal](./signal.md)

# signal

`fun signal(): `[`Signal`](../../com.pubnub.api.endpoints.pubsub/-signal/index.md)

Send a signal to all subscribers of a channel.

By default, signals are limited to a message payload size of 30 bytes.
This limit applies only to the payload, and not to the URI or headers.
If you require a larger payload size, please [contact support](mailto:support@pubnub.com).

