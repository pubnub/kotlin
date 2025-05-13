//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.util](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)/[instantToTimetoken](instant-to-timetoken.md)

# instantToTimetoken

[jvm]\

@[JvmStatic](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)

fun [instantToTimetoken](instant-to-timetoken.md)(instant: [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html)): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)

Converts [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html) to a PubNub timetoken

A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970.

#### Return

A 17-digit [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) representing the PubNub timetoken for the given [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html).

#### Parameters

jvm

| | |
|---|---|
| instant | The [Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html) object to be converted to a PubNub timetoken. |
