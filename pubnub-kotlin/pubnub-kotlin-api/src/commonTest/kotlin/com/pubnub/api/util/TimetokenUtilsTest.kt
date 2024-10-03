package com.pubnub.api.util

import com.pubnub.api.utils.TimetokenUtil
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TimetokenUtilsTest {
    @Test
    fun can_convert_timestamp_to_DateTime_then_from_DateTime_to_timestamp() {
        // given 2024-09-30 11:24:20.623211800
        val timetoken = 17276954606232118

        // when
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val timetokenResult = TimetokenUtil.instantToTimetoken(instant)

        // then
        val localDateTimeInUTC = instant.toLocalDateTime(TimeZone.UTC)
        assertEquals("2024-09-30", localDateTimeInUTC.date.toString())
        assertEquals("11:24:20.623211800", localDateTimeInUTC.time.toString())
        assertEquals(timetoken, timetokenResult)
    }

    @Test
    fun can_convert_unixTime_in_seconds_to_timetoken() {
        // given 2024-09-30 20:00:12
        val unixTime = 1727726412L

        // when
        val timetoken: Long = TimetokenUtil.unixToTimetoken(unixTime)

        // then
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTimeInUTC = instant.toLocalDateTime(TimeZone.UTC)
        assertEquals("2024-09-30", localDateTimeInUTC.date.toString())
        assertEquals("20:00:12", localDateTimeInUTC.time.toString())
    }

    @Test
    fun can_convert_unixTime_in_milliseconds_to_timetoken() {
        // given 2024-10-02 11:02:15.316
        val unixTime = 1727866935316

        // when
        val timetoken: Long = TimetokenUtil.unixToTimetoken(unixTime)

        // then
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTimeInUTC = instant.toLocalDateTime(TimeZone.UTC)
        assertEquals("2024-10-02", localDateTimeInUTC.date.toString())
        assertEquals("11:02:15.316", localDateTimeInUTC.time.toString())
    }

    @Test
    fun can_convert_unixTime_in_not_having_10_or_13_digits() {
        val unixTime4Digits = 1_000L
        val exception = assertFailsWith<IllegalArgumentException> {
            TimetokenUtil.unixToTimetoken(unixTime4Digits)
        }
        assertEquals("Unix timetoken should have 10 or 13 digits", exception.message)
    }

    @Test
    fun can_convert_timestamp_to_unixTime() {
        // given 2024-09-30 11:24:20.623211800
        val timetoken = 17276954606232118

        // wehn
        val unixTime = TimetokenUtil.timetokenToUnix(timetoken)

        // then
        val instant = Instant.fromEpochMilliseconds(unixTime)
        val toLocalDateTime = instant.toLocalDateTime(TimeZone.UTC)
        assertEquals("2024-09-30", toLocalDateTime.date.toString())
        assertEquals("11:24:20.623", toLocalDateTime.time.toString())
    }
}
