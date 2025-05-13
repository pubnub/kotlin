//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.utils](../index.md)/[Instant](index.md)

# Instant

[common]\
expect class [Instant](index.md) : [Comparable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-comparable/index.html)&lt;[Instant](index.md)&gt; actual typealias [Instant](index.md) = kotlinx.datetime.Instant

[js]\
actual class [Instant](index.md)(val epochSeconds: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), val nanosecondsOfSecond: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0) : [Comparable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-comparable/index.html)&lt;[Instant](index.md)&gt;

## Constructors

| | |
|---|---|
| [Instant](-instant.md) | [js]<br>constructor(epochSeconds: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html), nanosecondsOfSecond: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common, js]<br>[common]<br>expect object [Companion](-companion/index.md)<br>[js]<br>actual object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [epochSeconds](epoch-seconds.md) | [common, js]<br>[common]<br>expect val [epochSeconds](epoch-seconds.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)<br>[js]<br>actual val [epochSeconds](epoch-seconds.md): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) |
| [nanosecondsOfSecond](nanoseconds-of-second.md) | [common, js]<br>[common]<br>expect val [nanosecondsOfSecond](nanoseconds-of-second.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>[js]<br>actual val [nanosecondsOfSecond](nanoseconds-of-second.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0 |

## Functions

| Name | Summary |
|---|---|
| [compareTo](compare-to.md) | [common, js]<br>[common]<br>expect open operator override fun [compareTo](compare-to.md)(other: [Instant](index.md)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>[js]<br>actual open operator override fun [compareTo](compare-to.md)(other: [Instant](index.md)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [minus](minus.md) | [common, js]<br>[common]<br>expect operator fun [minus](minus.md)(other: [Instant](index.md)): [Duration](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/index.html)<br>[js]<br>actual operator fun [minus](minus.md)(other: [Instant](index.md)): [Duration](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/index.html)<br>[common]<br>expect operator fun [minus](minus.md)(duration: [Duration](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/index.html)): [Instant](index.md)<br>[js]<br>actual operator fun [minus](minus.md)(duration: [Duration](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/index.html)): [Instant](index.md) |
| [plus](plus.md) | [common, js]<br>[common]<br>expect operator fun [plus](plus.md)(duration: [Duration](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/index.html)): [Instant](index.md)<br>[js]<br>actual operator fun [plus](plus.md)(duration: [Duration](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-duration/index.html)): [Instant](index.md) |
| [toEpochMilliseconds](to-epoch-milliseconds.md) | [common, js]<br>[common]<br>expect fun [toEpochMilliseconds](to-epoch-milliseconds.md)(): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)<br>[js]<br>actual fun [toEpochMilliseconds](to-epoch-milliseconds.md)(): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) |
