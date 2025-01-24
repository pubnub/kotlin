//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.utils](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Clock](-clock/index.md) | [common, js]<br>[common]<br>expect interface [Clock](-clock/index.md)actual interface [Clock](-clock/index.md)<br>[js]<br>actual interface [Clock](-clock/index.md) |
| [Instant](-instant/index.md) | [common, js]<br>[common]<br>expect class [Instant](-instant/index.md) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-comparable/index.html)&lt;[Instant](-instant/index.md)&gt; actual typealias [Instant](-instant/index.md) = kotlinx.datetime.Instant<br>[js]<br>actual class [Instant](-instant/index.md)(val epochSeconds: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val nanosecondsOfSecond: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-comparable/index.html)&lt;[Instant](-instant/index.md)&gt; |
| [SecondsAndNanos](-seconds-and-nanos/index.md) | [js]<br>typealias [SecondsAndNanos](-seconds-and-nanos/index.md) = [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-pair/index.html)&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt; |
| [TimetokenUtil](-timetoken-util/index.md) | [common]<br>class [TimetokenUtil](-timetoken-util/index.md)<br>Utility object for converting PubNub timetokens to various date-time representations and vice versa. |

## Properties

| Name | Summary |
|---|---|
| [nanos](nanos.md) | [js]<br>val [SecondsAndNanos](-seconds-and-nanos/index.md).[nanos](nanos.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.time/-duration/index.html) |
| [seconds](seconds.md) | [js]<br>val [SecondsAndNanos](-seconds-and-nanos/index.md).[seconds](seconds.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.time/-duration/index.html) |
