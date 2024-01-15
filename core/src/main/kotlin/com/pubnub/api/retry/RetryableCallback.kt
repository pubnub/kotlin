package com.pubnub.api.retry

import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal abstract class RetryableCallback<T>(
    retryPolicy: RequestRetryPolicy,
    endpointGroupName: RetryableEndpointGroup,
    private val call: Call<T>,
    private val isEndpointRetryable: Boolean
) : Callback<T>, RetryableBase<T>(retryPolicy, endpointGroupName) {
    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)
    private var retryCount = 0
    private val random = Random.Default
    private var exponentialMultiplier = 0.0
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (shouldRetryOnResponse(response)) {
            retryOnResponseWithError(response)
        } else {
            executorService.shutdown()
            onFinalResponse(call, response)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (shouldRetryOnFailure(t)) {
            retryOnFailure()
        } else {
            executorService.shutdown()
            onFinalFailure(call, t)
        }
    }

    private fun shouldRetryOnResponse(response: Response<T>): Boolean {
        return !response.isSuccessful &&
            retryCount < maxRetryNumberFromConfiguration &&
            isErrorCodeRetryable(response.raw().code) &&
            isRetryPolicySetForThisRestCall &&
            isEndpointRetryable
    }

    private fun shouldRetryOnFailure(t: Throwable): Boolean {
        val exception = Exception(t)
        return retryCount < maxRetryNumberFromConfiguration &&
            isExceptionRetryable(exception) &&
            isRetryPolicySetForThisRestCall &&
            isEndpointRetryable
    }

    private fun isExceptionRetryable(e: Exception): Boolean {
        return e.cause?.let { cause ->
            retryableExceptions.any { it.isInstance(cause) }
        } ?: false
    }

    private fun retryOnFailure() {
        val effectiveDelay = getDelayForRetryOnFailure()
        retry(effectiveDelay)
    }

    private fun retryOnResponseWithError(response: Response<T>) {
        val effectiveDelay: Duration = getDelayForRetryOnResponse(response)
        retry(effectiveDelay)
    }

    private fun retry(delay: Duration) {
        retryCount++
        val randomDelayInMillis = random.nextInt(MAX_RANDOM_DELAY_IN_MILLIS)
        val effectiveDelayInMillis = delay.inWholeMilliseconds + randomDelayInMillis
        log.trace("Added random delay so effective retry delay is $effectiveDelayInMillis")
        // don't want to block the main thread in case of Android so using executor service
        executorService.execute {
            try {
                Thread.sleep(effectiveDelayInMillis)
                call.clone().enqueue(this)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun getDelayForRetryOnFailure(): Duration {
        return getDelayFromRetryPolicy().seconds
    }

    private fun getDelayForRetryOnResponse(response: Response<T>): Duration {
        return getDelayBasedOnResponse(response)
    }

    abstract fun onFinalResponse(call: Call<T>, response: Response<T>)
    abstract fun onFinalFailure(call: Call<T>, t: Throwable)
}
