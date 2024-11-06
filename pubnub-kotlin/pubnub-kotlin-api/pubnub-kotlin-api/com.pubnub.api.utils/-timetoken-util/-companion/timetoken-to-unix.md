//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.utils](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)/[timetokenToUnix](timetoken-to-unix.md)

# timetokenToUnix

[common]\
fun [timetokenToUnix](timetoken-to-unix.md)(timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Converts a PubNub timetoken to a Unix timestamp (in millis).

A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970. This function converts the PubNub timetoken to a Unix timestamp by reducing the precision to millis. Note that precision finer than millis is lost in this conversion.

#### Return

A [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) representing the Unix timestamp in millis corresponding to the given timetoken.

#### Parameters

common

| | |
|---|---|
| timetoken | The PubNub timetoken to be converted to a Unix timestamp. |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if the timetoken does not have 17 digits. |
