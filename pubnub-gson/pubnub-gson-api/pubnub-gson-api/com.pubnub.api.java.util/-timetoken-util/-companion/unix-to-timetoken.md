//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.util](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)/[unixToTimetoken](unix-to-timetoken.md)

# unixToTimetoken

[jvm]\

@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)

fun [unixToTimetoken](unix-to-timetoken.md)(unixTime: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)

Converts a Unix timestamp (in millis) to a PubNub timetoken

#### Return

A 17-digit [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) representing the PubNub timetoken corresponding to the given Unix timestamp.

#### Parameters

jvm

| | |
|---|---|
| unixTime | The Unix timestamp to be converted to a PubNub timetoken. |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html) | if the unixTime does not have 13 digits. |
