package com.pubnub.api.java.util;

import java.time.Instant;

public class TimetokenUtil {
    private static final long MINIMAL_TIMETOKEN_VALUE = 10_000_000_000_000_000L;
    private static final long MAXIMUM_TIMETOKEN_VALUE = 99_999_999_999_999_999L;
    private static final long MINIMAL_UNIX_TIME_VALUE = 1_000_000_000_000L;
    private static final long MAXIMUM_UNIX_TIME_VALUE = 9_999_999_999_999L;
    private static final int UNIX_TIME_LENGTH_WHEN_IN_MILLISECONDS = 13;

    /**
     * Converts a PubNub timetoken (a unique identifier for each message sent and received in a PubNub channel that is
     * a number of 100-nanosecond intervals since January 1, 1970) to Instant object representing
     * the corresponding date and time.
     *
     * @param timetoken PubNub timetoken
     * @return Instant representing the corresponding date and time.
     * @throws IllegalArgumentException if the timetoken does not have 17 digits.
     */
    public static Instant timetokenToInstant(long timetoken) {
        if (isLengthDifferentThan17Digits(timetoken)) {
            throw new IllegalArgumentException("Timetoken should have 17 digits");
        }

        // Convert timetoken to seconds and nanoseconds components
        long epochSeconds = timetoken / 10_000_000; // Divide by 10^7 to get seconds
        int epochNanoseconds = (int) ((timetoken % 10_000_000) * 100); // The remainder, multiplied by 100 to get nanoseconds

        return Instant.ofEpochSecond(epochSeconds, epochNanoseconds);
    }

    /**
     * Converts Instant to a PubNub timetoken.
     *
     * A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970.
     *
     * @param instant The Instant object to be converted to a PubNub timetoken.
     * @return A 17-digit long representing the PubNub timetoken for the given Instant.
     */
    public static long instantToTimetoken(Instant instant) {
        long epochSeconds = instant.getEpochSecond();
        int epochNanoseconds = instant.getNano();

        long pnTimetokenInNanosecondsWithoutNanoPrecision = epochSeconds * 10_000_000;
        long nanosecondsForPubNubTimetoken = epochNanoseconds / 100; // PubNub timetoken for nanoseconds store only 7 digits instead of 9

        return pnTimetokenInNanosecondsWithoutNanoPrecision + nanosecondsForPubNubTimetoken;
    }

    /**
     * Converts a Unix timestamp (in millis) to a PubNub timetoken.
     *
     * @param unixTime The Unix timestamp to be converted to a PubNub timetoken.
     * @return A 17-digit long representing the PubNub timetoken corresponding to the given Unix timestamp.
     * @throws IllegalArgumentException if the unixTime does not have 13 digits.
     */
    public static long unixToTimetoken(long unixTime) {
        if (isLengthDifferentThan13Digits(unixTime)) {
            throw new IllegalArgumentException("Unix timetoken should have " + UNIX_TIME_LENGTH_WHEN_IN_MILLISECONDS + " digits.");
        }

        return unixTime * 10_000; // PubNub timetoken has 17 digits
    }

    /**
     * Converts a PubNub timetoken to a Unix timestamp (in millis).
     *
     * A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970.
     * This function converts the PubNub timetoken to a Unix timestamp by reducing the precision to millis.
     * Note that precision finer than millis is lost in this conversion.
     *
     * @param timetoken The PubNub timetoken to be converted to a Unix timestamp.
     * @return A long representing the Unix timestamp in millis corresponding to the given timetoken.
     */
    public static long timetokenToUnix(long timetoken) {
        return timetoken / 10_000; // PubNub timetoken has 17 digits
    }

    private static boolean isLengthDifferentThan17Digits(long timetoken) {
        return timetoken < MINIMAL_TIMETOKEN_VALUE || timetoken > MAXIMUM_TIMETOKEN_VALUE;
    }

    private static boolean isLengthDifferentThan13Digits(long unixTime) {
        return unixTime < MINIMAL_UNIX_TIME_VALUE || unixTime > MAXIMUM_UNIX_TIME_VALUE;
    }
}