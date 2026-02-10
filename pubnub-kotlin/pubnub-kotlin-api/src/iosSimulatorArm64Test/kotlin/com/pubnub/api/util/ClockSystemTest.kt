@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.pubnub.api.util

import com.pubnub.api.utils.Clock
import com.pubnub.api.utils.Instant
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class ClockSystemTest {
    @Test
    fun now_isCloseToKotlinTimeClock() {
        val ktBefore = kotlin.time.Clock.System.now()
        val pn = Clock.System.now()
        val ktAfter = kotlin.time.Clock.System.now()

        val pnMillis = pn.toEpochMilliseconds()
        val beforeMillis =
            Instant.fromEpochSeconds(ktBefore.epochSeconds, ktBefore.nanosecondsOfSecond).toEpochMilliseconds()
        val afterMillis =
            Instant.fromEpochSeconds(ktAfter.epochSeconds, ktAfter.nanosecondsOfSecond).toEpochMilliseconds()

        // Should land between the two readings (with some slack)
        assertTrue(pnMillis >= beforeMillis - 5_000)
        assertTrue(pnMillis <= afterMillis + 5_000)
        assertTrue(abs(pnMillis - ((beforeMillis + afterMillis) / 2)) < 10_000)
    }

    @Test
    fun now_isNonDecreasing() {
        val a = Clock.System.now()
        val b = Clock.System.now()
        assertTrue(b >= a)
    }
}
