package com.pubnub.api.retry

import org.slf4j.LoggerFactory

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
     * @property excludedOperations Operations (Endpoints) to be excluded from retry.
     */
    class Linear(
        var delayInSec: Int = MIN_DELAY, // min value is 2
        var maxRetryNumber: Int = MAX_RETRIES_IN_LINEAR, // max value is 10
        val excludedOperations: List<RetryableEndpointGroup> = emptyList()
    ) : RetryConfiguration() {
        private val log = LoggerFactory.getLogger(this.javaClass.simpleName + "-" + "RequestRetryPolicy")

        init {
            if (delayInSec < MIN_DELAY) {
                log.trace("Provided delay is less than $MIN_DELAY, setting it to $MIN_DELAY")
                delayInSec = MIN_DELAY
            }
            if (maxRetryNumber > MAX_RETRIES_IN_LINEAR) {
                log.trace("Provided maxRetryNumber is greater than $MAX_RETRIES_IN_LINEAR, setting it to $MAX_RETRIES_IN_LINEAR")
                maxRetryNumber = MAX_RETRIES_IN_LINEAR
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
     * @property excludedOperations Operations (Endpoints) to exclude from retry.
     */
    class Exponential(
        var minDelayInSec: Int = MIN_DELAY, // min value is 2
        var maxDelayInSec: Int = MAX_DELAY, // max value is 150
        var maxRetryNumber: Int = MAX_RETRIES_IN_EXPONENTIAL, // max value is 6
        val excludedOperations: List<RetryableEndpointGroup> = emptyList()
    ) : RetryConfiguration() {
        private val log = LoggerFactory.getLogger(this.javaClass.simpleName + "-" + "RequestRetryPolicy")

        init {
            if (minDelayInSec < MIN_DELAY) {
                log.trace("Provided minDelayInSec is less than $MIN_DELAY, setting it to $MIN_DELAY")
                minDelayInSec = MIN_DELAY
            }
            if (minDelayInSec > MAX_DELAY) {
                log.trace("Provided minDelayInSec is greater than $MAX_DELAY, setting it to $MAX_DELAY")
                minDelayInSec = MAX_DELAY
            }
            if (maxDelayInSec > MAX_DELAY) {
                log.trace("Provided maxDelayInSec is greater than $MAX_DELAY, setting it to $MAX_DELAY")
                maxDelayInSec = MAX_DELAY
            }
            if (maxDelayInSec < minDelayInSec) {
                log.trace("Provided maxDelayInSec is less than minDelayInSec, setting it to $minDelayInSec")
                maxDelayInSec = minDelayInSec
            }
            if (maxRetryNumber > MAX_RETRIES_IN_EXPONENTIAL) {
                log.trace("Provided maxRetryNumber is greater than $MAX_RETRIES_IN_EXPONENTIAL, setting it to $MAX_RETRIES_IN_EXPONENTIAL")
                maxRetryNumber = MAX_RETRIES_IN_EXPONENTIAL
            }
        }
    }
}
