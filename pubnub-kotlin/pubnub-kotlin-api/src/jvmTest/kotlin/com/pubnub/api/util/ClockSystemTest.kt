package com.pubnub.api.util

import com.pubnub.api.utils.Clock
import kotlin.test.Test
import kotlin.test.assertTrue

class ClockSystemTest {
    @Test
    fun now_isCloseToWallClockTime() {
        val before = System.currentTimeMillis()
        val instant = Clock.System.now()
        val after = System.currentTimeMillis()

        val instantMillis = instant.toEpochMilliseconds()

        // Slack for CI / scheduling jitter
        assertTrue(instantMillis in (before - 5_000)..(after + 5_000))
    }

    @Test
    fun now_isNonDecreasing() {
        val a = Clock.System.now()
        val b = Clock.System.now()
        assertTrue(b >= a)
    }
}
