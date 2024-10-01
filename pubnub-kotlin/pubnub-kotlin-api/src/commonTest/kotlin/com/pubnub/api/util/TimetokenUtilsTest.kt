package com.pubnub.api.util

import com.pubnub.api.utils.TimetokenUtil
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class TimetokenUtilsTest {

    @Test
    fun can_convert_timestamp_to_DateTime_then_from_DateTime_to_timestamp() {
        // given 2024-09-30 11:24:20.623211800
        val timetoken = 17276954606232118

        // when
        val timetokenToDateTimeUTC: LocalDateTime = TimetokenUtil.timetokenToDateTimeUTC(timetoken)
        val timetokenResult = TimetokenUtil.dateTimeUTCToTimetoken(timetokenToDateTimeUTC)

        // then
        assertEquals("2024-09-30", timetokenToDateTimeUTC.date.toString())
        assertEquals("11:24:20.623211800", timetokenToDateTimeUTC.time.toString())
        assertEquals(timetoken, timetokenResult)
    }

    @Test
    fun can_convert_unitTime_to_timetoken() {
        // given 2024-09-30 20:00:12
        val unixTime = 1727726412.toLong()

        // when
        val timetoken = TimetokenUtil.unixToTimetoken(unixTime)

        // then
        val dateTimeUTC: LocalDateTime = TimetokenUtil.timetokenToDateTimeUTC(timetoken)
        assertEquals("2024-09-30", dateTimeUTC.date.toString())
        assertEquals("20:00:12", dateTimeUTC.time.toString())
    }

    @Test
    fun can_convert_timestamp_to_unixTime() {
        // given 2024-09-30 11:24:20.623211800
        val timetoken = 17276954606232118

        // wehn
        val unixTime = TimetokenUtil.timetokenToUnix(timetoken)

        // then
        val instant = Instant.fromEpochSeconds(unixTime)
        val toLocalDateTime = instant.toLocalDateTime(TimeZone.UTC)
        assertEquals("2024-09-30", toLocalDateTime.date.toString())
        assertEquals("11:24:20", toLocalDateTime.time.toString())
    }
}
