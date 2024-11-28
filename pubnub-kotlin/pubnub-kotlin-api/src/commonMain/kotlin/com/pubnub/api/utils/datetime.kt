package com.pubnub.api.utils

import kotlin.time.Duration

expect class Instant : Comparable<Instant> {
    val epochSeconds: Long
    val nanosecondsOfSecond: Int

    fun toEpochMilliseconds(): Long

    operator fun plus(duration: Duration): Instant

    operator fun minus(duration: Duration): Instant

    operator fun minus(other: Instant): Duration

    override operator fun compareTo(other: Instant): Int

    companion object {
        fun fromEpochMilliseconds(epochMilliseconds: Long): Instant

        fun fromEpochSeconds(epochSeconds: Long, nanosecondAdjustment: Int): Instant
    }
}

expect interface Clock {
    fun now(): Instant

    object System : Clock {
        override fun now(): Instant
    }
}
