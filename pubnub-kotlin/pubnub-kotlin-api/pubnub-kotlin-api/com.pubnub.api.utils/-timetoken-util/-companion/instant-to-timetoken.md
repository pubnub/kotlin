//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.utils](../../index.md)/[TimetokenUtil](../index.md)/[Companion](index.md)/[instantToTimetoken](instant-to-timetoken.md)

# instantToTimetoken

[common]\
fun [instantToTimetoken](instant-to-timetoken.md)(instant: Instant): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Converts Instant to a PubNub timetoken

A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970.

#### Return

A 17-digit [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) representing the PubNub timetoken for the given Instant.

#### Parameters

common

| | |
|---|---|
| instant | The Instant object to be converted to a PubNub timetoken. |
