//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[setPresenceTimeoutWithCustomInterval](set-presence-timeout-with-custom-interval.md)

# setPresenceTimeoutWithCustomInterval

[jvm]\
fun [setPresenceTimeoutWithCustomInterval](set-presence-timeout-with-custom-interval.md)(timeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), interval: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)

Set presence configurations for timeout and announce interval.

#### Return

returns itself.

#### Parameters

jvm

| | |
|---|---|
| timeout | presence timeout; how long before the server considers this client to be gone. |
| interval | presence announce interval, how often the client should announce itself. |
