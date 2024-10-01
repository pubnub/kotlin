package com.pubnub.api.utils

import kotlinx.datetime.Instant
import kotlin.math.pow

private const val MINIMA_TIMETOKE_VALUE = 10_000_000_000_000_000

private const val MAXIMUM_TIMETOKEN_VALUE = 99_999_999_999_999_999

private const val NUMBER_OF_DIGITS_IN_TIMETOKEN = 17

/**
 * Utility object for converting PubNub timetokens to various date-time representations and vice versa.
 *
 * This utility provides methods for converting between PubNub timetokens, Unix timestamps, and date-time objects.
 * A PubNub timetoken is a 17-digit number that represents the number of 100-nanosecond intervals since
 * January 1, 1970 (UTC). These methods allow for easy conversion between different time representations
 * used in PubNub, Unix, and standard date-time formats.
 */
object TimetokenUtil {
    /**
     * Converts a PubNub timetoken (a unique identifier for each message sent and received in a PubNub channel that is
     * a number of 100-nanosecond intervals since January 1, 1970) to LocalDateTime object representing
     * the corresponding date and time.
     *
     * @param timetoken PubNub timetoken
     * @return [Instant] representing the corresponding data and time.
     * @throws IllegalArgumentException if the timetoken does not have 17 digits.
     */
    fun timetokenToInstant(timetoken: Long): Instant {
        if (isLengthDifferentThan17Digits(timetoken)) {
            throw IllegalArgumentException("Timetoken should have 17 digits")
        }
        // Convert timetoken to seconds and nanoseconds components
        val epochSeconds = timetoken / 10_000_000 // Divide by 10^7 to get seconds
        val epochNanoseconds =
            ((timetoken % 10_000_000) * 100).toInt() // The remainder, multiplied by 100 to get nanoseconds

        return Instant.fromEpochSeconds(epochSeconds, epochNanoseconds)
    }

    /**
     * Converts [Instant] to a PubNub timetoken
     *
     * A PubNub timetoken is a 17-digit number representing the number of 100-nanosecond intervals since January 1, 1970.
     *
     * @param instant The [Instant] object to be converted to a PubNub timetoken.
     * @return A 17-digit [Long] representing the PubNub timetoken for the given [Instant].
     */
    fun instantToTimetoken(instant: Instant): Long {
        val epochSeconds: Long = instant.epochSeconds
        val epochNanoseconds: Int = instant.nanosecondsOfSecond

        val pnTimetokenInNanosecondsWithoutNanoPrecision = epochSeconds * 10_000_000
        val nanosecondsForPubNubTimetoken =
            epochNanoseconds / 100 // PubNub timetoken for nanoseconds store only 7 digits instead of 9
        return pnTimetokenInNanosecondsWithoutNanoPrecision + nanosecondsForPubNubTimetoken
    }

    /**
     * Converts a Unix timestamp to a PubNub timetoken
     *
     * @param unixTime The Unix timestamp to be converted to a PubNub timetoken.
     * @return A 17-digit [Long] representing the PubNub timetoken corresponding to the given Unix timestamp.
     */
    fun unixToTimetoken(unixTime: Long): Long {
        val numberOfDigits = unixTime.toString().length
        val zerosToBeAdded = NUMBER_OF_DIGITS_IN_TIMETOKEN - numberOfDigits
        return unixTime * 10.toDouble().pow(zerosToBeAdded).toLong()
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
     */
    fun timetokenToUnix(timetoken: Long): Long {
        return (timetoken / 10_000)
    }

    private fun isLengthDifferentThan17Digits(timetoken: Long): Boolean {
        return timetoken !in MINIMA_TIMETOKE_VALUE..MAXIMUM_TIMETOKEN_VALUE
    }
}
