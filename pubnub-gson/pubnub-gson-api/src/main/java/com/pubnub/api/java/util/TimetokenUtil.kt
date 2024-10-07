package com.pubnub.api.java.util

import java.time.Instant

/**
 * Utility object for converting PubNub timetokens to various date-time representations and vice versa.
 *
 * This utility provides methods for converting between PubNub timetokens, Unix timestamps, and date-time objects.
 * A PubNub timetoken is a 17-digit number that represents the number of 100-nanosecond intervals since
 * January 1, 1970 (UTC). These methods allow for easy conversion between different time representations
 * used in PubNub, Unix, and standard date-time formats.
 */
class TimetokenUtil {
    companion object {
        private const val MINIMAL_TIMETOKE_VALUE = 10_000_000_000_000_000
        private const val MAXIMUM_TIMETOKEN_VALUE = 99_999_999_999_999_999
        private const val MINIMAL_UNIX_TIME_VALUE = 1_000_000_000_000
        private const val MAXIMUM_UNIX_TIME_VALUE = 9_999_999_999_999
        private const val UNIX_TIME_LENGHT_WHEN_IN_MILLISECONDS = 13
        private const val SECONDS_CONVERSION_FACTOR: Int = 10_000_000
        private const val NANOSECONDS_CONVERSION_FACTOR: Int = 100
        private const val UNIX_TO_TIMETOKEN_CONVERSION_FACTOR: Int = 10_000

        /**
         * Converts a PubNub timetoken (a unique identifier for each message sent and received in a PubNub channel that is
         * a number of 100-nanosecond intervals since January 1, 1970) to LocalDateTime object representing
         * the corresponding moment in time.
         *
         * @param timetoken PubNub timetoken
         * @return [Instant] representing the corresponding moment in time.
         * @throws IllegalArgumentException if the timetoken does not have 17 digits.
         */
        @JvmStatic
        fun timetokenToInstant(timetoken: Long): Instant {
            if (isLengthDifferentThan17Digits(timetoken)) {
                throw IllegalArgumentException("Timetoken should have 17 digits")
            }
            // Convert timetoken to seconds and nanoseconds components
            val epochSeconds = timetoken / SECONDS_CONVERSION_FACTOR // Divide by 10^7 to get seconds
            val epochNanoseconds = (timetoken % SECONDS_CONVERSION_FACTOR) * NANOSECONDS_CONVERSION_FACTOR // The remainder, multiplied by 100 to get nanoseconds
            return Instant.ofEpochSecond(epochSeconds, epochNanoseconds)
        }

        /**
         * Converts [Instant] to a PubNub timetoken
         *
         * A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970.
         *
         * @param instant The [Instant] object to be converted to a PubNub timetoken.
         * @return A 17-digit [Long] representing the PubNub timetoken for the given [Instant].
         */
        @JvmStatic
        fun instantToTimetoken(instant: Instant): Long {
            val epochSeconds: Long = instant.epochSecond
            val epochNanoseconds: Int = instant.nano

            val pnTimetokenInNanosecondsWithoutNanoPrecision = epochSeconds * SECONDS_CONVERSION_FACTOR
            val nanosecondsForPubNubTimetoken = epochNanoseconds / NANOSECONDS_CONVERSION_FACTOR // PubNub timetoken for nanoseconds store only 7 digits instead of 9
            return pnTimetokenInNanosecondsWithoutNanoPrecision + nanosecondsForPubNubTimetoken
        }

        /**
         * Converts a Unix timestamp (in millis) to a PubNub timetoken
         *
         * @param unixTime The Unix timestamp to be converted to a PubNub timetoken.
         * @return A 17-digit [Long] representing the PubNub timetoken corresponding to the given Unix timestamp.
         * @throws IllegalArgumentException if the unixTime does not have 13 digits.
         */
        @JvmStatic
        fun unixToTimetoken(unixTime: Long): Long {
            if (isLengthDifferentThan13Digits(unixTime)) {
                throw IllegalArgumentException("Unix timetoken should have $UNIX_TIME_LENGHT_WHEN_IN_MILLISECONDS digits.")
            }
            return unixTime * UNIX_TO_TIMETOKEN_CONVERSION_FACTOR // PubNub timetoken has 17 digits
        }

        /**
         * Converts a PubNub timetoken to a Unix timestamp (in millis).
         *
         * A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970.
         * This function converts the PubNub timetoken to a Unix timestamp by reducing the precision to millis.
         * Note that precision finer than millis is lost in this conversion.
         *
         * @param timetoken The PubNub timetoken to be converted to a Unix timestamp.
         * @return A [Long] representing the Unix timestamp in millis corresponding to the given timetoken.
         * @throws IllegalArgumentException if the timetoken does not have 17 digits.
         */
        @JvmStatic
        fun timetokenToUnix(timetoken: Long): Long {
            if (isLengthDifferentThan17Digits(timetoken)) {
                throw IllegalArgumentException("Timetoken should have 17 digits")
            }
            return (timetoken / UNIX_TO_TIMETOKEN_CONVERSION_FACTOR) // PubNub timetoken has 17 digits
        }

        private fun isLengthDifferentThan17Digits(timetoken: Long): Boolean {
            return timetoken !in MINIMAL_TIMETOKE_VALUE..MAXIMUM_TIMETOKEN_VALUE
        }

        private fun isLengthDifferentThan13Digits(unixTime: Long): Boolean {
            return unixTime !in MINIMAL_UNIX_TIME_VALUE..MAXIMUM_UNIX_TIME_VALUE
        }
    }
}
