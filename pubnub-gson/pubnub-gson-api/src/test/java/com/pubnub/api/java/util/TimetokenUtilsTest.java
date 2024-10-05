package com.pubnub.api.java.util;

import org.junit.jupiter.api.Test;
//import com.pubnub.api.utils.TimetokenUtil;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimetokenUtilsTest {

    @Test
    public void canConvertTimestampToDateTimeThenFromDateTimeToTimestamp() {

        // given 2024-09-30 11:24:20.623211800
        long timetoken = 17276954606232118L;

        // when
        Instant instant = TimetokenUtil.timetokenToInstant(timetoken);
        long timetokenResult = TimetokenUtil.instantToTimetoken(instant);

        // then
        LocalDateTime localDateTimeInUTC = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        assertEquals("2024-09-30", localDateTimeInUTC.toLocalDate().toString());
        assertEquals("11:24:20.623211800", localDateTimeInUTC.toLocalTime().toString());
        assertEquals(timetoken, timetokenResult);
    }

    @Test
    public void canConvertUnixTimeInMillisecondsToTimetoken() {
        // given 2024-10-02 11:02:15.316
        long unixTime = 1727866935316L;

        // when
        long timetoken = TimetokenUtil.unixToTimetoken(unixTime);

        // then
        Instant instant = TimetokenUtil.timetokenToInstant(timetoken);
        LocalDateTime localDateTimeInUTC = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        assertEquals("2024-10-02", localDateTimeInUTC.toLocalDate().toString());
        assertEquals("11:02:15.316", localDateTimeInUTC.toLocalTime().toString());
    }

    @Test
    public void canConvertUnixTimeNotHaving10Or13Digits() {
        long unixTime4Digits = 1_000_000_000L;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TimetokenUtil.unixToTimetoken(unixTime4Digits);
        });
        assertEquals("Unix timetoken should have 13 digits.", exception.getMessage());
    }

    @Test
    public void canConvertTimestampToUnixTime() {
        // given 2024-09-30 11:24:20.623211800
        long timetoken = 17276954606232118L;

        // when
        long unixTime = TimetokenUtil.timetokenToUnix(timetoken);

        // then
        Instant instant = Instant.ofEpochMilli(unixTime);
        LocalDateTime toLocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        assertEquals("2024-09-30", toLocalDateTime.toLocalDate().toString());
        assertEquals("11:24:20.623", toLocalDateTime.toLocalTime().toString());
    }
}

