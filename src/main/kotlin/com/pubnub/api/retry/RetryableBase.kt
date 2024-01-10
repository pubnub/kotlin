package com.pubnub.api.retry

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.math.pow

internal abstract class RetryableBase<T>(
    val retryPolicy: RequestRetryPolicy,
    val endpointGroupName: RetryableEndpointGroup
) {

    companion object {
        const val BOUND = 1000 // random delay should be between 0 and 1000 milliseconds
        private const val TOO_MANY_REQUEST = 429
        const val SERVICE_UNAVAILABLE = 503
        private const val RETRY_AFTER_HEADER_NAME = "Retry-After"
        const val MILLISECONDS = 1000
        private val retryableStatusCodes = mapOf(
            429 to "TOO_MANY_REQUESTS",
            500 to "HTTP_INTERNAL_ERROR",
            501 to "HTTP_NOT_IMPLEMENTED",
            502 to "HTTP_BAD_GATEWAY",
            503 to "HTTP_UNAVAILABLE",
            504 to "HTTP_GATEWAY_TIMEOUT",
            505 to "HTTP_VERSION",
        )
        internal val retryableExceptions = listOf(
            UnknownHostException::class.java,
            SocketTimeoutException::class.java,
            ConnectException::class.java,
            SSLHandshakeException::class.java,
        )
    }

    private var exponentialMultiplier = 0.0

    internal fun isRetryPolicySetForThisRestCall(): Boolean {
        return when (retryPolicy) {
            is RequestRetryPolicy.None -> false

            is RequestRetryPolicy.Linear -> {
                val excludedOperations = retryPolicy.excludedOperations
                endpointIsNotExcludedFromRetryPolicy(excludedOperations)
            }
            is RequestRetryPolicy.Exponential -> {
                val excludedOperations = retryPolicy.excludedOperations
                endpointIsNotExcludedFromRetryPolicy(excludedOperations)
            }
        }
    }

    internal fun getDelayInMilliSeconds(response: Response<T>): Int {
        val effectiveDelay = if (response.raw().code == TOO_MANY_REQUEST) {
            calculateDelayForTooManyRequestError(response)
        } else {
            val delayFromRetryPolicyInMilliSec = getDelayFromRetryPolicy() * MILLISECONDS
            delayFromRetryPolicyInMilliSec
        }
        return effectiveDelay
    }

    internal fun getDelayFromRetryPolicy(): Int {
        return when (retryPolicy) {
            is RequestRetryPolicy.None -> 0
            is RequestRetryPolicy.Linear -> retryPolicy.delayInSec
            is RequestRetryPolicy.Exponential -> {
                val delay: Int = (retryPolicy.minDelayInSec * 2.0.pow(exponentialMultiplier)).toInt()
                exponentialMultiplier++
                minOf(delay, retryPolicy.maxDelayInSec)
            }
        }
    }

    internal fun getRetryCount(): Int {
        return when (retryPolicy) {
            is RequestRetryPolicy.None -> 0
            is RequestRetryPolicy.Linear -> retryPolicy.maxRetryNumber
            is RequestRetryPolicy.Exponential -> retryPolicy.maxRetryNumber
        }
    }

    internal fun isErrorCodeRetryable(errorCode: Int) = retryableStatusCodes.containsKey(errorCode)

    private fun calculateDelayForTooManyRequestError(
        response: Response<T>
    ): Int {
        val retryAfterInSec: String? = response.headers()[RETRY_AFTER_HEADER_NAME]
        if (!retryAfterInSec.isNullOrBlank() && !isNumeric(retryAfterInSec)) {
            throw PubNubException(PubNubError.RETRY_AFTER_HEADER_VALUE_CAN_NOT_BE_PARSED_TO_INT)
        }
        return retryAfterInSec?.toIntOrNull()?.let { retryAfterInSec ->
            if (retryAfterInSec > 0) {
                retryAfterInSec * MILLISECONDS
            } else {
                getDelayFromRetryPolicy() * MILLISECONDS
            }
        } ?: (getDelayFromRetryPolicy() * MILLISECONDS)
    }

    private fun endpointIsNotExcludedFromRetryPolicy(excludedOperations: List<RetryableEndpointGroup>): Boolean =
        excludedOperations.contains(endpointGroupName).not()

    private fun isNumeric(string: String?): Boolean {
        return string?.toIntOrNull() != null
    }
}
