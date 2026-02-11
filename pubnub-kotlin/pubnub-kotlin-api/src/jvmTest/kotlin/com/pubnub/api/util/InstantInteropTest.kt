package com.pubnub.api.util

import com.pubnub.api.utils.Instant
import com.pubnub.api.utils.TimetokenUtil
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class InstantInteropTest {
    @Test
    fun pubnubInstant_canBeUsedWithKotlinxDatetimeExtensions() {
        val unixMillis = 1_700_000_000_000L // deterministic timestamp
        val timetoken = TimetokenUtil.unixToTimetoken(unixMillis)

        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val ldt = instant.toLocalDateTime(TimeZone.UTC)

        assertEquals(2023, ldt.year)
        assertEquals(Month.NOVEMBER, ldt.month)
        assertEquals(14, ldt.day)
        assertEquals(22, ldt.hour)
        assertEquals(13, ldt.minute)
        assertEquals(20, ldt.second)
    }
}
