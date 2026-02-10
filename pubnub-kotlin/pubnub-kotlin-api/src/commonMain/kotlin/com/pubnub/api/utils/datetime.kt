package com.pubnub.api.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

data class Instant(
    val epochSeconds: Long,
    val nanosecondsOfSecond: Int = 0,
) : Comparable<Instant> {
    init {
        require(nanosecondsOfSecond in 0..999_999_999) {
            "nanosecondsOfSecond must be in range 0..999_999_999 but was $nanosecondsOfSecond"
        }
    }

    fun toEpochMilliseconds(): Long {
        return epochSeconds * 1_000L + nanosecondsOfSecond / 1_000_000
    }

    @OptIn(kotlin.time.ExperimentalTime::class)
    fun toLocalDateTime(timeZone: TimeZone): LocalDateTime {
        val ktInstant = kotlin.time.Instant.fromEpochSeconds(epochSeconds, nanosecondsOfSecond)
        return ktInstant.toLocalDateTime(timeZone)
    }

    operator fun plus(duration: Duration): Instant {
        val durationSeconds = duration.inWholeSeconds
        val durationNanosRemainder = (duration - durationSeconds.seconds).inWholeNanoseconds
        return normalize(epochSeconds + durationSeconds, nanosecondsOfSecond.toLong() + durationNanosRemainder)
    }

    operator fun minus(duration: Duration): Instant = plus(-duration)

    operator fun minus(other: Instant): Duration {
        return epochSeconds.seconds + nanosecondsOfSecond.nanoseconds - other.epochSeconds.seconds - other.nanosecondsOfSecond.nanoseconds
    }

    override operator fun compareTo(other: Instant): Int {
        return epochSeconds.compareTo(other.epochSeconds)
            .takeIf { it != 0 }
            ?: nanosecondsOfSecond.compareTo(other.nanosecondsOfSecond)
    }

    companion object {
        fun fromEpochMilliseconds(epochMilliseconds: Long): Instant {
            val seconds = floorDiv(epochMilliseconds, 1_000L)
            val millisRemainder = floorMod(epochMilliseconds, 1_000L)
            return Instant(seconds, (millisRemainder * 1_000_000L).toInt())
        }

        fun fromEpochSeconds(epochSeconds: Long, nanosecondAdjustment: Int): Instant {
            return normalize(epochSeconds, nanosecondAdjustment.toLong())
        }

        private const val NANOS_PER_SECOND = 1_000_000_000L

        private fun normalize(epochSeconds: Long, nanoAdjustment: Long): Instant {
            val carrySeconds = floorDiv(nanoAdjustment, NANOS_PER_SECOND)
            val normalizedNanos = floorMod(nanoAdjustment, NANOS_PER_SECOND)
            return Instant(epochSeconds + carrySeconds, normalizedNanos.toInt())
        }

        private fun floorDiv(x: Long, y: Long): Long {
            val r = x / y
            val m = x % y
            return if (m == 0L || (x xor y) >= 0) r else r - 1
        }

        private fun floorMod(x: Long, y: Long): Long = x - floorDiv(x, y) * y
    }
}

expect interface Clock {
    fun now(): Instant

    object System : Clock {
        override fun now(): Instant
    }
}