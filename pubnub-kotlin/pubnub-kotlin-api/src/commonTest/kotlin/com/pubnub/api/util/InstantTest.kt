package com.pubnub.api.util

import com.pubnub.api.utils.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class InstantTest {
    @Test
    fun plusDuration() {
        val nowMillis = 1732793616984
        val now = Instant.fromEpochMilliseconds(nowMillis)
        val later = now + 1500.milliseconds

        assertEquals(nowMillis + 1500, later.toEpochMilliseconds())

        val laterWithNanos = now + 2000.nanoseconds
        assertEquals(now.epochSeconds, laterWithNanos.epochSeconds)
        assertEquals(now.nanosecondsOfSecond + 2000, laterWithNanos.nanosecondsOfSecond)

        val laterWithSecondsAndNanos = now + (1.seconds + 2000.nanoseconds)
        assertEquals(now.epochSeconds + 1, laterWithSecondsAndNanos.epochSeconds)
        assertEquals(now.nanosecondsOfSecond + 2000, laterWithSecondsAndNanos.nanosecondsOfSecond)
    }

    @Test
    fun minusDuration() {
        val nowMillis = 1732793616984
        val now = Instant.fromEpochMilliseconds(nowMillis)
        val later = now - 1500.milliseconds

        assertEquals(nowMillis - 1500, later.toEpochMilliseconds())

        val laterWithNanos = now - 2000.nanoseconds
        assertEquals(now.epochSeconds, laterWithNanos.epochSeconds)
        assertEquals(now.nanosecondsOfSecond - 2000, laterWithNanos.nanosecondsOfSecond)

        val laterWithSecondsAndNanos = now - (1.seconds + 2000.nanoseconds)
        assertEquals(now.epochSeconds - 1, laterWithSecondsAndNanos.epochSeconds)
        assertEquals(now.nanosecondsOfSecond - 2000, laterWithSecondsAndNanos.nanosecondsOfSecond)
    }

    @Test
    fun minusInstant() {
        val nowMillis = 1732793616984
        val laterMillis = 1732793616984 + 1500
        val now = Instant.fromEpochMilliseconds(nowMillis)
        val later = Instant.fromEpochMilliseconds(laterMillis)

        assertEquals(1500.milliseconds, later - now)
        assertEquals(-1500.milliseconds, now - later)
    }

    @Test
    fun compareTo() {
        val nowMillis = 1732793616984
        val laterMillis = 1732793616984 + 1500
        val now = Instant.fromEpochMilliseconds(nowMillis)
        val later = Instant.fromEpochMilliseconds(laterMillis)

        assertTrue(now < later)
        assertTrue(later > now)

        assertTrue(now + 100.nanoseconds > now)
        assertTrue(now - 100.nanoseconds < now)
    }
}
