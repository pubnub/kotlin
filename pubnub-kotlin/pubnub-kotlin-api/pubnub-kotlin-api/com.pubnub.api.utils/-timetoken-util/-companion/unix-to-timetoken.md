//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.utils](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)/[unixToTimetoken](unix-to-timetoken.md)

# unixToTimetoken

[common]\
fun [unixToTimetoken](unix-to-timetoken.md)(unixTime: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)

Converts a Unix timestamp (in millis) to a PubNub timetoken

#### Return

A 17-digit [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) representing the PubNub timetoken corresponding to the given Unix timestamp.

#### Parameters

common

| | |
|---|---|
| unixTime | The Unix timestamp to be converted to a PubNub timetoken. |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if the unixTime does not have 13 digits. |
