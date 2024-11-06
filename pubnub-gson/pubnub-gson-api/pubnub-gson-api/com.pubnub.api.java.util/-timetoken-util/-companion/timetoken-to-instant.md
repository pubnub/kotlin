//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.util](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)/[timetokenToInstant](timetoken-to-instant.md)

# timetokenToInstant

[jvm]\

@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)

fun [timetokenToInstant](timetoken-to-instant.md)(timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html)

Converts a PubNub timetoken (a unique identifier for each message sent and received in a PubNub channel that is a number of 100-nanosecond intervals since January 1, 1970) to LocalDateTime object representing the corresponding moment in time.

#### Return

[Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html) representing the corresponding moment in time.

#### Parameters

jvm

| | |
|---|---|
| timetoken | PubNub timetoken |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if the timetoken does not have 17 digits. |
