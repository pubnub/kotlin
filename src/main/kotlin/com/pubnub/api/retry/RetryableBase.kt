package com.pubnub.api.retry

import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal abstract class RetryableBase<T>(
    private val retryConfiguration: RetryConfiguration,
    private val endpointGroupName: RetryableEndpointGroup
) {

    companion object {
        const val MAX_RANDOM_DELAY_IN_MILLIS = 1000 // random delay should be between 0 and 1000 milliseconds
        private const val TOO_MANY_REQUESTS = 429
        const val SERVICE_UNAVAILABLE = 503
        private const val RETRY_AFTER_HEADER_NAME = "Retry-After"
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

    internal val isRetryConfSetForThisRestCall = when (retryConfiguration) {
        is RetryConfiguration.None -> false

        is RetryConfiguration.Linear -> {
            val excludedOperations = retryConfiguration.excludedOperations
            endpointIsNotExcludedFromRetryPolicy(excludedOperations)
        }
        is RetryConfiguration.Exponential -> {
            val excludedOperations = retryConfiguration.excludedOperations
            endpointIsNotExcludedFromRetryPolicy(excludedOperations)
        }
    }

    internal fun getDelayBasedOnResponse(response: Response<T>): Duration {
        val effectiveDelay: Duration = if (response.raw().code == TOO_MANY_REQUESTS) {
            calculateDelayForTooManyRequestError(response)
        } else {
            getDelayFromRetryConfiguration().seconds
        }
        return effectiveDelay
    }

    internal fun getDelayFromRetryConfiguration(): Int {
        return when (retryConfiguration) {
            is RetryConfiguration.None -> 0
            is RetryConfiguration.Linear -> retryConfiguration.delayInSec
            is RetryConfiguration.Exponential -> {
                val delay: Int = (retryConfiguration.minDelayInSec * 2.0.pow(exponentialMultiplier)).toInt()
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

    internal fun isErrorCodeRetryable(errorCode: Int) = retryableStatusCodes.containsKey(errorCode)

    private fun calculateDelayForTooManyRequestError(
        response: Response<T>
    ): Duration {
        val retryAfterInSec: String? = response.headers()[RETRY_AFTER_HEADER_NAME]
        val delayInSeconds = retryAfterInSec?.toIntOrNull()

        return when {
            delayInSeconds != null && delayInSeconds > 0 -> {
                delayInSeconds.seconds
            }
            else -> {
                getDelayFromRetryConfiguration().seconds
            }
        }
    }

    private fun endpointIsNotExcludedFromRetryPolicy(excludedOperations: List<RetryableEndpointGroup>): Boolean =
        excludedOperations.contains(endpointGroupName).not()
}
