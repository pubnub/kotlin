package com.pubnub.internal.retry

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.PubNubRetryableException
import com.pubnub.internal.logging.LoggerManager
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Response

internal class RetryableRestCaller<T>(
    retryConfiguration: RetryConfiguration,
    endpointGroupName: RetryableEndpointGroup,
    private val isEndpointRetryable: Boolean,
    private val logConfig: LogConfig,
) : RetryableBase<T>(retryConfiguration, endpointGroupName) {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    internal lateinit var call: Call<T>

    internal fun execute(callToBeExecuted: Call<T>): Response<T> {
        call = callToBeExecuted
        var numberOfAttempts = 0
        while (true) {
            val (response, exception) = executeRestCall()
            if (!shouldRetry(response) || numberOfAttempts++ >= maxRetryNumberFromConfiguration) {
                // http issue can be represented by error code inside unsuccessful response or by exception
                // if it is represented by error code then we want to pass response for further processing
                if (response.isSuccessful || exception == null) {
                    return response
                } else {
                    throw exception
                }
            }
            val randomDelayInMillis = random.nextInt(MAX_RANDOM_DELAY_IN_MILLIS)
            val effectiveDelayInMillis = getDelayBasedOnResponse(response).inWholeMilliseconds + randomDelayInMillis
            log.trace(
                LogMessage(
                    message = LogMessageContent.Text("Added random delay so effective retry delay is $effectiveDelayInMillis"),
                )
            )
            Thread.sleep(effectiveDelayInMillis) // we want to sleep here on current thread since this is synchronous call

            call = call.clone()
        }
    }

    private fun executeRestCall(): Pair<Response<T>, Exception?> {
        var pubNubException: Exception? = null
        try {
            return try {
                val response = call.execute()
                Pair(response, pubNubException)
            } catch (e: Exception) {
                pubNubException =
                    PubNubException(
                        pubnubError = PubNubError.PARSING_ERROR,
                        errorMessage = e.toString(),
                        affectedCall = call,
                    )

                if (isExceptionRetryable(e)) {
                    throw PubNubRetryableException(
                        pubnubError = PubNubError.CONNECT_EXCEPTION,
                        errorMessage = e.toString(),
                        statusCode = SERVICE_UNAVAILABLE, // all retryable exceptions internally are mapped to 503 error
                    )
                } else {
                    throw pubNubException
                }
            }
        } catch (e: PubNubRetryableException) {
            return Pair(
                Response.error(e.statusCode, e.errorMessage?.toResponseBody() ?: "".toResponseBody()),
                pubNubException,
            )
        }
    }

    private fun isExceptionRetryable(e: Exception): Boolean {
        return retryableExceptions.any { it.isInstance(e) }
    }

    private fun shouldRetry(response: Response<T>) =
        !response.isSuccessful &&
            isErrorCodeRetryable(response.raw().code) &&
            isRetryConfSetForThisRestCall &&
            isEndpointRetryable
}
