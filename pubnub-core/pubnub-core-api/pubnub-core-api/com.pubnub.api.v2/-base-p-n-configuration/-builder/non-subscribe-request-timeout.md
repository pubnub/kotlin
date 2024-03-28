//[pubnub-core-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[BasePNConfiguration](../index.md)/[Builder](index.md)/[nonSubscribeRequestTimeout](non-subscribe-request-timeout.md)

# nonSubscribeRequestTimeout

[jvm]\
abstract val [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

For non subscribe operations (publish, herenow, etc), This property relates to a read timeout that is applied from the moment the connection between a client and the server has been successfully established. It defines a maximum time of inactivity between two data packets when waiting for the server’s response.

The value is in seconds.

Defaults to 10.
