[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PNConfiguration](index.md) / [nonSubscribeRequestTimeout](./non-subscribe-request-timeout.md)

# nonSubscribeRequestTimeout

`var nonSubscribeRequestTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

For non subscribe operations (publish, herenow, etc),
how long to wait to connect to PubNub before giving up with a connection timeout error.

The value is in seconds.

Defaults to 10.

