package com.pubnub.api.retry

//import org.jetbrains.annotations.TestOnly
//import org.slf4j.LoggerFactory
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val MIN_DELAY = 2
private const val MAX_DELAY = 150

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
    class Linear private constructor(
        var delayInSec: Duration = MIN_DELAY.seconds,
        var maxRetryNumber: Int = MAX_RETRIES,
        val excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        isInternal: Boolean = false,
    ) : RetryConfiguration() {
//        private val log = LoggerFactory.getLogger(this.javaClass.simpleName + "-" + "RetryConfiguration")

        constructor(
            delayInSec: Int = MIN_DELAY,
            maxRetryNumber: Int = MAX_RETRIES,
            excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        ) : this(delayInSec.seconds, maxRetryNumber, excludedOperations, false)

        // additional constructors for java
        constructor() : this(MIN_DELAY, MAX_RETRIES, emptyList())
        constructor(delayInSec: Int) : this(delayInSec, MAX_RETRIES, emptyList())
        constructor(delayInSec: Int, maxRetryNumber: Int) : this(delayInSec, maxRetryNumber, emptyList())

        init {
            if (!isInternal) {
                if (delayInSec < MIN_DELAY.seconds) {
//                    log.trace("Provided delay is less than $MIN_DELAY, setting it to $MIN_DELAY")
                    delayInSec = MIN_DELAY.seconds
                }
                if (maxRetryNumber > MAX_RETRIES) {
//                    log.trace("Provided maxRetryNumber is greater than $MAX_RETRIES, setting it to $MAX_RETRIES")
                    maxRetryNumber = MAX_RETRIES
                }
            }
        }

        companion object {
            @JvmSynthetic
//            @TestOnly
            internal fun createForTest(
                delayInSec: Duration = MIN_DELAY.seconds,
                maxRetryNumber: Int = MAX_RETRIES,
                excludedOperations: List<RetryableEndpointGroup> = emptyList(),
                isInternal: Boolean = false,
            ): Linear = Linear(delayInSec, maxRetryNumber, excludedOperations, isInternal)

            const val MAX_RETRIES = 10
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
    class Exponential private constructor(
        var minDelayInSec: Duration = MIN_DELAY.seconds,
        var maxDelayInSec: Duration = MAX_DELAY.seconds,
        var maxRetryNumber: Int = MAX_RETRIES,
        val excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        isInternal: Boolean = false,
    ) : RetryConfiguration() {
//        private val log = LoggerFactory.getLogger(this.javaClass.simpleName + "-" + "RetryConfiguration")

        constructor(
            minDelayInSec: Int = MIN_DELAY,
            maxDelayInSec: Int = MAX_DELAY,
            maxRetryNumber: Int = MAX_RETRIES,
            excludedOperations: List<RetryableEndpointGroup> = emptyList(),
        ) : this(minDelayInSec.seconds, maxDelayInSec.seconds, maxRetryNumber, excludedOperations, false)

        // additional constructors for java
        constructor() : this(MIN_DELAY, MAX_DELAY, MAX_RETRIES, emptyList())
        constructor(minDelayInSec: Int, maxDelayInSec: Int) : this(
            minDelayInSec,
            maxDelayInSec,
            MAX_RETRIES,
            emptyList(),
        )

        constructor(minDelayInSec: Int, maxDelayInSec: Int, maxRetryNumber: Int) : this(
            minDelayInSec,
            maxDelayInSec,
            maxRetryNumber,
            emptyList(),
        )

        init {
            if (!isInternal) {
                val originalMinDelayInSec = minDelayInSec
                val originalMaxDelayInSec = maxDelayInSec
                val originalMaxRetryNumber = maxRetryNumber

                minDelayInSec = minDelayInSec.coerceIn(MIN_DELAY.seconds, MAX_DELAY.seconds)
                maxDelayInSec = maxDelayInSec.coerceAtLeast(minDelayInSec).coerceAtMost(MAX_DELAY.seconds)
                maxRetryNumber = maxRetryNumber.coerceAtMost(MAX_RETRIES)

                if (minDelayInSec != originalMinDelayInSec || maxDelayInSec != originalMaxDelayInSec || maxRetryNumber != originalMaxRetryNumber) {
//                    log.trace("Adjusted values: minDelayInSec=$minDelayInSec, maxDelayInSec=$maxDelayInSec, maxRetryNumber=$maxRetryNumber")
                }
            }
        }

        companion object {
            @JvmSynthetic
//            @TestOnly
            internal fun createForTest(
                minDelayInSec: Duration = MIN_DELAY.seconds,
                maxDelayInSec: Duration = MAX_DELAY.seconds,
                maxRetryNumber: Int = MAX_RETRIES,
                excludedOperations: List<RetryableEndpointGroup> = emptyList(),
                isInternal: Boolean = false,
            ): Exponential = Exponential(minDelayInSec, maxDelayInSec, maxRetryNumber, excludedOperations, isInternal)

            const val MAX_RETRIES = 6
        }
    }
}
