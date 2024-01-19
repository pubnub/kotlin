package com.pubnub.internal.retry

import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.math.pow
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal abstract class RetryableBase<T>(
    private val retryConfiguration: RetryConfiguration,
    private val endpointGroupName: RetryableEndpointGroup
) {

    companion object {
        const val MAX_RANDOM_DELAY_IN_MILLIS = 1000 // random delay should be between 0 and 1000 milliseconds
        private const val TOO_MANY_REQUESTS = 429
        const val SERVICE_UNAVAILABLE = 503
        internal const val RETRY_AFTER_HEADER_NAME = "Retry-After"
        private val retryableStatusCodes = mapOf(
            429 to "TOO_MANY_REQUESTS",
            500 to "HTTP_INTERNAL_ERROR",
            502 to "HTTP_BAD_GATEWAY",
            503 to "HTTP_UNAVAILABLE",
            504 to "HTTP_GATEWAY_TIMEOUT",
            507 to "INSUFFICIENT_STORAGE",
            508 to "LOOP_DETECTED",
            510 to "NOT_EXTENDED",
            511 to "NETWORK_AUTHENTICATION_REQUIRED",
        )
        internal val retryableExceptions = listOf(
            UnknownHostException::class.java,
            SocketTimeoutException::class.java,
            ConnectException::class.java,
            SSLHandshakeException::class.java,
            IOException::class.java
        )
    }

    private var exponentialMultiplier = 0.0
    protected val random = Random.Default

    internal val isRetryConfSetForThisRestCall = when (retryConfiguration) {
        is RetryConfiguration.None -> false

        is RetryConfiguration.Linear -> {
            val excludedOperations = retryConfiguration.excludedOperations
            endpointIsNotExcludedFromRetryConfiguration(excludedOperations)
        }
        is RetryConfiguration.Exponential -> {
            val excludedOperations = retryConfiguration.excludedOperations
            endpointIsNotExcludedFromRetryConfiguration(excludedOperations)
        }
    }

    internal fun getDelayBasedOnResponse(response: Response<T>): Duration {
        val effectiveDelay: Duration = if (response.raw().code == TOO_MANY_REQUESTS) {
            calculateDelayForTooManyRequestError(response)
        } else {
            getDelayFromRetryConfiguration()
        }
        return effectiveDelay
    }

    private fun getDelayBasedOnErrorCode(errorCode: Int, retryAfterHeaderValueInSec: Int): Duration {
        return if (errorCode == TOO_MANY_REQUESTS) {
            getDelayForRetryAfterHeaderValue(retryAfterHeaderValueInSec)
        } else {
            getDelayFromRetryConfiguration()
        }
    }

    internal fun getDelayFromRetryConfiguration(): Duration {
        return when (retryConfiguration) {
            is RetryConfiguration.None -> 0.seconds
            is RetryConfiguration.Linear -> retryConfiguration.delayInSec
            is RetryConfiguration.Exponential -> {
                val delay: Duration = retryConfiguration.minDelayInSec * 2.0.pow(exponentialMultiplier)
                exponentialMultiplier++
                minOf(delay, retryConfiguration.maxDelayInSec)
            }
        }
    }

    protected val maxRetryNumberFromConfiguration: Int = when (retryConfiguration) {
        is RetryConfiguration.None -> 0
        is RetryConfiguration.Linear -> retryConfiguration.maxRetryNumber
        is RetryConfiguration.Exponential -> retryConfiguration.maxRetryNumber
    }

    protected fun isErrorCodeRetryable(errorCode: Int) = retryableStatusCodes.containsKey(errorCode)

    protected fun shouldRetry(attempts: Int): Boolean {
        return attempts < maxRetryNumberFromConfiguration
    }

    protected fun getEffectiveDelay(statusCode: Int, retryAfterHeaderValue: Int): Duration {
        val delayBasedOnStatusCode = getDelayBasedOnErrorCode(
            errorCode = statusCode,
            retryAfterHeaderValueInSec = retryAfterHeaderValue
        )
        val randomDelayInMillis = random.nextInt(MAX_RANDOM_DELAY_IN_MILLIS).milliseconds
        return delayBasedOnStatusCode + randomDelayInMillis
    }

    private fun calculateDelayForTooManyRequestError(
        response: Response<T>
    ): Duration {
        val retryAfterInSec: String? = response.headers()[RETRY_AFTER_HEADER_NAME]
        val delayInSeconds = retryAfterInSec?.toIntOrNull()

        return getDelayForRetryAfterHeaderValue(delayInSeconds)
    }

    private fun getDelayForRetryAfterHeaderValue(delayInSeconds: Int?): Duration {
        return when {
            delayInSeconds != null && delayInSeconds > 0 -> {
                delayInSeconds.seconds
            }
            else -> {
                getDelayFromRetryConfiguration()
            }
        }
    }

    private fun endpointIsNotExcludedFromRetryConfiguration(excludedOperations: List<RetryableEndpointGroup>): Boolean =
        excludedOperations.contains(endpointGroupName).not()
}
