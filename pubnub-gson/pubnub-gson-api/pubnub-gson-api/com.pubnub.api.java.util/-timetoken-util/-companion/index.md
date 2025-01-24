//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.util](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)

# Companion

[jvm]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [instantToTimetoken](instant-to-timetoken.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [instantToTimetoken](instant-to-timetoken.md)(instant: [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br>Converts [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html) to a PubNub timetoken |
| [timetokenToInstant](timetoken-to-instant.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [timetokenToInstant](timetoken-to-instant.md)(timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html)<br>Converts a PubNub timetoken (a unique identifier for each message sent and received in a PubNub channel that is a number of 100-nanosecond intervals since January 1, 1970) to LocalDateTime object representing the corresponding moment in time. |
| [timetokenToUnix](timetoken-to-unix.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [timetokenToUnix](timetoken-to-unix.md)(timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br>Converts a PubNub timetoken to a Unix timestamp (in millis). |
| [unixToTimetoken](unix-to-timetoken.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [unixToTimetoken](unix-to-timetoken.md)(unixTime: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br>Converts a Unix timestamp (in millis) to a PubNub timetoken |
