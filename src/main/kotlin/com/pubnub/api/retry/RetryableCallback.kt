package com.pubnub.api.retry

import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

internal abstract class RetryableCallback<T>(
    retryPolicy: RequestRetryPolicy,
    endpointGroupName: RetryableEndpointGroup,
    private val call: Call<T>,
    private val isEndpointRetryable: Boolean
) : Callback<T>, RetryableBase<T>(retryPolicy, endpointGroupName) {
    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)
    private var retryCount = 0
    private val maxRetryNumber = getRetryCount()
    private val random = Random.Default
    private var exponentialMultiplier = 0.0

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (shouldRetryOnResponse(response)) {
            retryOnResponseWithError(response)
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
            isRetryPolicySetForThisRestCall() &&
            isEndpointRetryable
    }

    private fun shouldRetryOnFailure(t: Throwable): Boolean {
        val exception = Exception(t)
        return retryCount < maxRetryNumber &&
            isExceptionRetryable(exception) &&
            isRetryPolicySetForThisRestCall() &&
            isEndpointRetryable
    }

    private fun isExceptionRetryable(e: Exception): Boolean {
        return e.cause?.let { cause ->
            retryableExceptions.any { it.isInstance(cause) }
        } ?: false
    }

    private fun retryOnFailure() {
        val effectiveDelay = getDelayInMilliSecForRetryOnFailure()
        retry(effectiveDelay)
    }

    private fun retryOnResponseWithError(response: Response<T>) {
        val effectiveDelay = getDelayInMilliSecForRetryOnResponse(response)
        retry(effectiveDelay)
    }

    private fun retry(delayInMilliSec: Int) {
        retryCount++
        val randomDelayInMilliSec = random.nextInt(BOUND)
        val effectiveDelay = delayInMilliSec + randomDelayInMilliSec
        log.trace("Added random delay so effective retry delay is $effectiveDelay")
        Thread.sleep(effectiveDelay.toLong())
        call.clone().enqueue(this)
    }

    private fun getDelayInMilliSecForRetryOnFailure(): Int {
        return getDelayFromRetryPolicy() * MILLISECONDS
    }

    private fun getDelayInMilliSecForRetryOnResponse(response: Response<T>): Int {
        return getDelayInMilliSeconds(response)
    }

    abstract fun onFinalResponse(call: Call<T>, response: Response<T>)
    abstract fun onFinalFailure(call: Call<T>, t: Throwable)
}
