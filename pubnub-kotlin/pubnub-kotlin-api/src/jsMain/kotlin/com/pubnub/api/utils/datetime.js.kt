package com.pubnub.api.utils

import kotlin.js.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

actual class Instant(
    actual val epochSeconds: Long,
    actual val nanosecondsOfSecond: Int = 0,
) : Comparable<Instant> {
    actual fun toEpochMilliseconds(): Long {
        return epochSeconds.seconds.inWholeMilliseconds + nanosecondsOfSecond.nanoseconds.inWholeMilliseconds
    }

    actual operator fun plus(duration: Duration): Instant {
        val durationWholeSecondsOnly = duration.inWholeSeconds.seconds
        val durationNanosOnly = duration - durationWholeSecondsOnly
        val sum = add(epochSeconds to nanosecondsOfSecond, duration.inWholeSeconds to durationNanosOnly.inWholeNanoseconds.toInt())
        return Instant(sum.first, sum.second)
    }

    actual operator fun minus(duration: Duration): Instant {
        return plus(-duration)
    }

    actual operator fun minus(other: Instant): Duration {
        return epochSeconds.seconds + nanosecondsOfSecond.nanoseconds - other.epochSeconds.seconds - other.nanosecondsOfSecond.nanoseconds
    }

    actual override operator fun compareTo(other: Instant): Int {
        return epochSeconds.compareTo(other.epochSeconds)
            .takeIf { it != 0 }
            ?: nanosecondsOfSecond.compareTo(other.nanosecondsOfSecond)
    }

    private fun add(secondsAndNanos: SecondsAndNanos, secondsAndNanos2: SecondsAndNanos): Pair<Long, Int> {
        val nanosSum = secondsAndNanos.nanos + secondsAndNanos2.nanos
        val secondsFromNanos = nanosSum.inWholeSeconds.seconds

        val secondsResult = secondsAndNanos.seconds + secondsAndNanos2.seconds + secondsFromNanos
        val nanosResult = nanosSum - secondsFromNanos
        return secondsResult.inWholeSeconds to nanosResult.inWholeNanoseconds.toInt()
    }

    actual companion object {
        actual fun fromEpochMilliseconds(epochMilliseconds: Long): Instant {
            val wholeSeconds = epochMilliseconds.milliseconds.inWholeSeconds
            val nanos = (epochMilliseconds.milliseconds - wholeSeconds.seconds).inWholeNanoseconds
            return Instant(wholeSeconds, nanos.toInt())
        }

        actual fun fromEpochSeconds(epochSeconds: Long, nanosecondAdjustment: Int): Instant {
            return Instant(epochSeconds, nanosecondAdjustment)
        }
    }
}

actual interface Clock {
    actual fun now(): Instant

    actual object System : Clock {
        actual override fun now(): Instant {
            return Instant.fromEpochMilliseconds(Date.now().toLong())
        }
    }
}

typealias SecondsAndNanos = Pair<Long, Int>

val SecondsAndNanos.seconds get() = this.first.seconds
val SecondsAndNanos.nanos get() = this.second.nanoseconds
