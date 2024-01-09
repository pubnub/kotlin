package com.pubnub.api.policies

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.pow
import kotlin.random.Random

abstract class RetryableCallback<T>(
    private val call: Call<T>,
    private val retryPolicy: RequestRetryPolicy,
    private val endpointGroupName: RetryableEndpointGroup,
    private val isEndpointRetryable: Boolean
) : Callback<T> {
    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)
    private var retryCount = 0
    private val maxRetryNumber = getRetryCount(retryPolicy)
    private val random = Random.Default
    private var exponentialMultiplier = 0.0

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (shouldRetryOnResponse(response)) {
            retryOnResponse(response)
        } else {
            onFinalResponse(call, response)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (shouldRetryOnFailure(t)) {
            retryOnFailure()
        } else {
            onFinalFailure(call, t)
        }
    }

    private fun shouldRetryOnResponse(response: Response<T>): Boolean {
        return !response.isSuccessful &&
            retryCount < maxRetryNumber &&
            isErrorCodeRetryable(response.raw().code) &&
            isRetryPolicySetForThisRestCall(retryPolicy) &&
            isEndpointRetryable
    }

    // todo duplicate
    private fun isErrorCodeRetryable(errorCode: Int) = Endpoint.retryableStatusCodes.containsKey(errorCode)

    private fun shouldRetryOnFailure(t: Throwable): Boolean {
        val exception = Exception(t)
        return retryCount < maxRetryNumber &&
            isExceptionRetryable(exception) &&
            isRetryPolicySetForThisRestCall(retryPolicy) &&
            isEndpointRetryable
    }

    private fun isExceptionRetryable(e: Exception): Boolean {
        return e.cause?.let { cause ->
            Endpoint.retryableExceptions.any { it.isInstance(cause) }
        } ?: false
    }

    private fun retryOnFailure() {
        retryCount++
        val randomDelayInMilliSec = random.nextInt(Endpoint.BOUND)
        val effectiveDelay = getEffectiveDelayInMilliSecondsForRetryOnFailure(randomDelayInMilliSec)
        Thread.sleep(effectiveDelay.toLong())
        call.clone().enqueue(this)
    }

    private fun retryOnResponse(response: Response<T>) {
        retryCount++
        val randomDelayInMilliSec = random.nextInt(Endpoint.BOUND)
        val effectiveDelay = getEffectiveDelayInMilliSecondsForRetryOnResponse(response, randomDelayInMilliSec)
        Thread.sleep(effectiveDelay.toLong())
        call.clone().enqueue(this)
    }

    // może skrócić MilliSeconds MilliSec?
    private fun getEffectiveDelayInMilliSecondsForRetryOnFailure(randomDelayInMilliSec: Int): Int {
        val delayFromRetryPolicyInMilliSec = getDelay(retryPolicy) * Endpoint.MILLISECONDS
        val effectiveDelay = delayFromRetryPolicyInMilliSec + randomDelayInMilliSec

        log.trace("Added random delay so effective retry delay is $effectiveDelay")
        return effectiveDelay
    }

    // todo duplicate, może skrócić MilliSeconds MilliSec?
    private fun getEffectiveDelayInMilliSecondsForRetryOnResponse(
        response: Response<T>,
        randomDelayInMilliSec: Int
    ): Int {
        val effectiveDelay = if (response.raw().code == Endpoint.TOO_MANY_REQUEST) {
            val retryAfterInSec: String? = response.headers()[Endpoint.RETRY_AFTER_HEADER_NAME]
            if (!isNullOrEmpty(retryAfterInSec) && !isNumeric(retryAfterInSec)) {
                throw PubNubException(PubNubError.RETRY_AFTER_HEADER_VALUE_CAN_NOT_BE_PARSED_TO_INT)
            }
            if (retryAfterInSec?.toInt() != null && retryAfterInSec.toInt() > 0) {
                val retryAfterDelayInMilliSec = retryAfterInSec.toInt() * Endpoint.MILLISECONDS
                retryAfterDelayInMilliSec + randomDelayInMilliSec
            } else {
                val delayFromRetryPolicyInMilliSec = getDelay(retryPolicy) * Endpoint.MILLISECONDS
                delayFromRetryPolicyInMilliSec + randomDelayInMilliSec
            }
        } else {
            val delayFromRetryPolicyInMilliSec = getDelay(retryPolicy) * Endpoint.MILLISECONDS
            delayFromRetryPolicyInMilliSec + randomDelayInMilliSec
        }

        log.trace("Added random delay so effective retry delay is $effectiveDelay")
        return effectiveDelay
    }

    // todo duplicate
    private fun isNullOrEmpty(string: String?): Boolean {
        return string == null || string.isBlank()
    }

    // todo duplicate
    private fun isNumeric(string: String?): Boolean {
        return string?.toIntOrNull() != null
    }

    // todo duplicate
    private fun isRetryPolicySetForThisRestCall(retryPolicy: RequestRetryPolicy): Boolean {
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

    // todo duplicate
    private fun getDelay(retryPolicy: RequestRetryPolicy): Int {
        return when (retryPolicy) {
            is RequestRetryPolicy.None -> 0
            is RequestRetryPolicy.Linear -> retryPolicy.delayInSec
            is RequestRetryPolicy.Exponential -> {
                val delay: Int = (retryPolicy.minDelayInSec * 2.0.pow(exponentialMultiplier)).toInt()
                exponentialMultiplier++
                if (delay > retryPolicy.maxDelayInSec) {
                    retryPolicy.maxDelayInSec
                } else {
                    delay
                }
            }
        }
    }

    // todo duplicate, przenieść do osobnej klasy klasy?
    private fun endpointIsNotExcludedFromRetryPolicy(excludedOperations: List<RetryableEndpointGroup>): Boolean =
        excludedOperations.contains(endpointGroupName).not()

    abstract fun onFinalResponse(call: Call<T>, response: Response<T>)
    abstract fun onFinalFailure(call: Call<T>, t: Throwable)

    // todo duplicate
    private fun getRetryCount(retryPolicy: RequestRetryPolicy): Int {
        return when (retryPolicy) {
            is RequestRetryPolicy.None -> 0
            is RequestRetryPolicy.Linear -> retryPolicy.maxRetryNumber
            is RequestRetryPolicy.Exponential -> retryPolicy.maxRetryNumber
        }
    }
}
