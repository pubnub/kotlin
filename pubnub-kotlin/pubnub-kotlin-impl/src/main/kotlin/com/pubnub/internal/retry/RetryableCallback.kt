package com.pubnub.internal.retry

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.extension.scheduleWithDelay
import com.pubnub.internal.logging.LoggerManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal abstract class RetryableCallback<T>(
    retryConfiguration: RetryConfiguration,
    endpointGroupName: RetryableEndpointGroup,
    private val call: Call<T>,
    private val isEndpointRetryable: Boolean,
    private val executorService: ScheduledExecutorService,
    private val logConfig: LogConfig,
) : Callback<T>, RetryableBase<T>(retryConfiguration, endpointGroupName) {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)
    private var retryCount = 0
    private var exponentialMultiplier = 0.0

    override fun onResponse(
        call: Call<T>,
        response: Response<T>,
    ) {
        if (shouldRetryOnResponse(response)) {
            retryOnResponseWithError(response)
        } else {
            onFinalResponse(call, response)
        }
    }

    override fun onFailure(
        call: Call<T>,
        t: Throwable,
    ) {
        if (shouldRetryOnFailure(t)) {
            retryOnFailure()
        } else {
            onFinalFailure(call, t)
        }
    }

    private fun shouldRetryOnResponse(response: Response<T>): Boolean {
        return !response.isSuccessful &&
            retryCount < maxRetryNumberFromConfiguration &&
            isErrorCodeRetryable(response.raw().code) &&
            isRetryConfSetForThisRestCall &&
            isEndpointRetryable
    }

    private fun shouldRetryOnFailure(t: Throwable): Boolean {
        val exception = Exception(t)
        return retryCount < maxRetryNumberFromConfiguration &&
            isExceptionRetryable(exception) &&
            isRetryConfSetForThisRestCall &&
            isEndpointRetryable
    }

    private fun isExceptionRetryable(e: Exception): Boolean {
        return e.cause?.let { cause ->
            retryableExceptions.any { it.isInstance(cause) }
        } ?: false
    }

    private fun retryOnFailure() {
        val effectiveDelay = getDelayFromRetryConfiguration()
        retry(effectiveDelay)
    }

    private fun retryOnResponseWithError(response: Response<T>) {
        val effectiveDelay: Duration = getDelayForRetryOnResponse(response)
        retry(effectiveDelay)
    }

    private fun retry(delay: Duration) {
        retryCount++
        val randomDelayInMillis: Int = random.nextInt(MAX_RANDOM_DELAY_IN_MILLIS)
        val effectiveDelay: Duration = delay + randomDelayInMillis.milliseconds
        log.trace(
            LogMessage(
                location = this::class.java.simpleName,
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text(
                    "Added random delay so effective retry delay is ${effectiveDelay.inWholeMilliseconds} millis"
                )
            )
        )
        // don't want to block the main thread in case of Android so using executorService
        try {
            executorService.scheduleWithDelay(effectiveDelay) {
                call.clone().enqueue(this)
            }
        } catch (_: RejectedExecutionException) {
            log.trace(
                LogMessage(
                    location = this::class.java.simpleName,
                    type = LogMessageType.TEXT,
                    message = LogMessageContent.Text("Unable to schedule retry, PubNub was likely already destroyed.")
                )
            )
        }
    }

    private fun getDelayForRetryOnResponse(response: Response<T>): Duration {
        return getDelayBasedOnResponse(response)
    }

    abstract fun onFinalResponse(
        call: Call<T>,
        response: Response<T>,
    )

    abstract fun onFinalFailure(
        call: Call<T>,
        t: Throwable,
    )
}
