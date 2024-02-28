package com.pubnub.api.retry

import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val MIN_DELAY = 2
private const val MAX_DELAY = 150
private const val MAX_RETRIES_IN_LINEAR = 10
private const val MAX_RETRIES_IN_EXPONENTIAL = 6

/**
 * This sealed class represents the various retry policies for a request.
 */
sealed class RetryConfiguration {
    /**
     * None represents no retry policy in a network request
     */
    object None : RetryConfiguration()

    /**
     * This data class represents a linear retry policy for network requests with a delay between retries,
     * a maximum number of retries and a list of operations to exclude from retries.
     *
     * @property delayInSec The delay in seconds between retries. Minimum value is 3 seconds.
     * @property maxRetryNumber The maximum number of retries allowed. Maximum value is 10.
     * @property excludedOperations List of [RetryableEndpointGroup] to be excluded from retry.
     */
    class Linear(
        var delayInSec: Duration = MIN_DELAY.seconds,
        var maxRetryNumber: Int = MAX_RETRIES_IN_LINEAR,
        val excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        isInternal: Boolean = false,
    ) : RetryConfiguration() {
        private val log = LoggerFactory.getLogger(this.javaClass.simpleName + "-" + "RetryConfiguration")

        constructor(
            delayInSec: Int = MIN_DELAY, // min value is 2
            maxRetryNumber: Int = MAX_RETRIES_IN_LINEAR, // max value is 10
            excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        ) : this(delayInSec.seconds, maxRetryNumber, excludedOperations, false)

        init {
            if (!isInternal) {
                if (delayInSec < MIN_DELAY.seconds) {
                    log.trace("Provided delay is less than $MIN_DELAY, setting it to $MIN_DELAY")
                    delayInSec = MIN_DELAY.seconds
                }
                if (maxRetryNumber > MAX_RETRIES_IN_LINEAR) {
                    log.trace("Provided maxRetryNumber is greater than $MAX_RETRIES_IN_LINEAR, setting it to $MAX_RETRIES_IN_LINEAR")
                    maxRetryNumber = MAX_RETRIES_IN_LINEAR
                }
            }
        }
    }

    /**
     * This class represents an exponential retry policy with a minimum and
     * maximum delay between retries, a maximum number of retries, and a list of
     * operations to exclude from retry attempts.
     *
     * @property minDelayInSec The minimum delay in seconds between retries. Minimum value is 3 seconds.
     * @property maxDelayInSec The maximum delay in seconds between retries.
     * @property maxRetryNumber The maximum number of retries allowed. Maximum value is 10.
     * @property excludedOperations List of [RetryableEndpointGroup] to be excluded from retry.
     */
    class Exponential internal constructor(
        var minDelayInSec: Duration = MIN_DELAY.seconds, // min value is 2
        var maxDelayInSec: Duration = MAX_DELAY.seconds, // max value is 150
        var maxRetryNumber: Int = MAX_RETRIES_IN_EXPONENTIAL, // max value is 6
        val excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        isInternal: Boolean = false,
    ) : RetryConfiguration() {
        private val log = LoggerFactory.getLogger(this.javaClass.simpleName + "-" + "RetryConfiguration")

        constructor(
            minDelayInSec: Int = MIN_DELAY, // min value is 2
            maxDelayInSec: Int = MAX_DELAY, // max value is 150
            maxRetryNumber: Int = MAX_RETRIES_IN_EXPONENTIAL, // max value is 6
            excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        ) : this(minDelayInSec.seconds, maxDelayInSec.seconds, maxRetryNumber, excludedOperations, false)

        init {
            if (!isInternal) {
                val originalMinDelayInSec = minDelayInSec
                val originalMaxDelayInSec = maxDelayInSec
                val originalMaxRetryNumber = maxRetryNumber

                minDelayInSec = minDelayInSec.coerceIn(MIN_DELAY.seconds, MAX_DELAY.seconds)
                maxDelayInSec = maxDelayInSec.coerceAtLeast(minDelayInSec).coerceAtMost(MAX_DELAY.seconds)
                maxRetryNumber = maxRetryNumber.coerceAtMost(MAX_RETRIES_IN_EXPONENTIAL)

                if (minDelayInSec != originalMinDelayInSec || maxDelayInSec != originalMaxDelayInSec || maxRetryNumber != originalMaxRetryNumber) {
                    log.trace("Adjusted values: minDelayInSec=$minDelayInSec, maxDelayInSec=$maxDelayInSec, maxRetryNumber=$maxRetryNumber")
                }
            }
        }
    }
}
