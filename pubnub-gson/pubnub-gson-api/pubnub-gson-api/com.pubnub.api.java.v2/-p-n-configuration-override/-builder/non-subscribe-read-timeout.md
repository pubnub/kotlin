//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfigurationOverride](../index.md)/[Builder](index.md)/[nonSubscribeReadTimeout](non-subscribe-read-timeout.md)

# nonSubscribeReadTimeout

[jvm]\
abstract fun [nonSubscribeReadTimeout](non-subscribe-read-timeout.md)(nonSubscribeReadTimeout: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [PNConfigurationOverride.Builder](index.md)

For non subscribe operations (publish, herenow, etc), This property relates to a read timeout that is applied from the moment the connection between a client and the server has been successfully established. It defines a maximum time of inactivity between two data packets when waiting for the server’s response.

The value is in seconds.

Defaults to 10.
