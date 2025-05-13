//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.utils](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)/[timetokenToInstant](timetoken-to-instant.md)

# timetokenToInstant

[common]\
fun [timetokenToInstant](timetoken-to-instant.md)(timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)): [Instant](../../-instant/index.md)

Converts a PubNub timetoken (a unique identifier for each message sent and received in a PubNub channel that is a number of 100-nanosecond intervals since January 1, 1970) to LocalDateTime object representing the corresponding moment in time.

#### Return

[Instant](../../-instant/index.md) representing the corresponding moment in time.

#### Parameters

common

| | |
|---|---|
| timetoken | PubNub timetoken |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if the timetoken does not have 17 digits. |
