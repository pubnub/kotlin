package com.pubnub.api.policies

import org.slf4j.LoggerFactory
import kotlin.random.Random

private const val MIN_DELAY = 3.0
private const val MAX_RETRIES = 10

/**
 * This sealed class represents the various retry policies for a request.
 */
sealed class RequestRetryPolicy {

    /**
     * None represents no retry policy in a network request
     */
    object None : RequestRetryPolicy()

    /**
     * This data class represents a linear retry policy for network requests with a delay between retries,
     * a maximum number of retries and a list of operations to exclude from retries.
     *
     * @property delayInSec The delay in seconds between retries. Minimum value is 3 seconds.
     * @property maxRetryNumber The maximum number of retries allowed. Maximum value is 10.
     * @property excludedOperations Operations (Endpoints) to be excluded from retry.
     */
    class Linear(
        delayInSec: Int,
        var maxRetryNumber: Int,
        val excludedOperations: List<String>? = null // todo String --> Endpoint
    ) : RequestRetryPolicy() {
        private val log = LoggerFactory.getLogger(this.javaClass.simpleName + "-" + "RequestRetryPolicy")
        var delay: Double = delayInSec.toDouble()

        init {
            if (delay < MIN_DELAY) {
                log.trace("Provided delay is less than 3.0, setting it to 3.0")
                delay = MIN_DELAY
            }
            val randomDelay = Random.nextDouble(0.0, 3.0)
            delay = (delay + randomDelay).roundTo2DecimalPlaces()
            log.trace("Added random delay so effective retry delay is $delay")

            if (maxRetryNumber > MAX_RETRIES) {
                log.trace("Max retry number is greater than 10, setting it to 10")
                maxRetryNumber = MAX_RETRIES
            }
        }

        private fun Double.roundTo2DecimalPlaces() = "%.2f".format(this).toDouble()
    }

    /**
     * This class represents an exponential retry policy with a minimum and
     * maximum delay between retries, a maximum number of retries, and a list of
     * operations to exclude from retry attempts.
     *
     * @property minDelayInSec The minimum delay in seconds between retries. Minimum value is 3 seconds.
     * @property maxDelayInSec The maximum delay in seconds between retries.
     * @property maxRetry The maximum number of retries allowed. Maximum value is 10.
     * @property excludedOperations Operations (Endpoints) to exclude from retry.
     */
    class Exponential(
        var minDelayInSec: Int, // todo add random seconds between 1.0 and 3.0(with floating point)
        val maxDelayInSec: Int,
        var maxRetryNumber: Int,
        val excludedOperations: List<String>? = null // todo String --> Endpoint
    ) : RequestRetryPolicy() {
        init {
            if (minDelayInSec < 3) {
                minDelayInSec = 3
            }
            if (maxRetryNumber > 10) {
                maxRetryNumber = 10
            }
        }
    }
}
