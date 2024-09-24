//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[presenceTimeout](presence-timeout.md)

# presenceTimeout

[jvm]\
abstract fun [presenceTimeout](presence-timeout.md)(presenceTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration.Builder](index.md)

abstract val [presenceTimeout](presence-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Sets the custom presence server timeout.

The value is in seconds, and the minimum value is 20 seconds.

Also sets the value of [heartbeatInterval](heartbeat-interval.md)
