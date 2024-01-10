package com.pubnub.api.retry

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubRetryableException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Response
import kotlin.random.Random

internal class RetryableRestCaller<T>(
    retryPolicy: RequestRetryPolicy,
    endpointGroupName: RetryableEndpointGroup,
    private val isEndpointRetryable: Boolean
) : RetryableBase<T>(retryPolicy, endpointGroupName) {
    private companion object {
        private const val NUMBER_OF_REGULAR_CALLS = 1
    }

    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)
    private val random = Random.Default
    internal lateinit var call: Call<T>

    internal fun executeRestCallWithRetryPolicy(callToBeExecuted: Call<T>): Response<T> {
        call = callToBeExecuted
        var numberOfAttempts = 0
        val maxRetryNumber = getRetryCount()
        val maxAttempts = NUMBER_OF_REGULAR_CALLS + maxRetryNumber
        while (true) {
            val response = executeRestCall()
            if (!shouldRetry(response) || numberOfAttempts++ >= maxAttempts) {
                return response
            }
            val randomDelayInMilliSec = random.nextInt(BOUND)
            val effectiveDelay = getDelayInMilliSeconds(response) + randomDelayInMilliSec
            log.trace("Added random delay so effective retry delay is $effectiveDelay")
            Thread.sleep(effectiveDelay.toLong()) // we want to sleep here since this is synchronous call

            call = call.clone()
        }
    }

    private fun executeRestCall(): Response<T> {
        try {
            return try {
                call.execute()
            } catch (e: Exception) {
                if (isExceptionRetryable(e)) {
                    throw PubNubRetryableException(
                        pubnubError = PubNubError.CONNECT_EXCEPTION,
                        errorMessage = e.toString(),
                        statusCode = SERVICE_UNAVAILABLE // all retryable exceptions internally are mapped to 503 error
                    )
                } else {
                    throw PubNubException(
                        pubnubError = PubNubError.PARSING_ERROR,
                        errorMessage = e.toString(),
                        affectedCall = call
                    )
                }
            }
        } catch (e: PubNubRetryableException) {
            return Response.error(e.statusCode, e.errorMessage?.toResponseBody() ?: "".toResponseBody())
        } catch (e: Exception) {
            throw e
        }
    }

    private fun isExceptionRetryable(e: Exception): Boolean {
        return retryableExceptions.any { it.isInstance(e) }
    }

    private fun shouldRetry(response: Response<T>) =
        !response.isSuccessful &&
            isErrorCodeRetryable(response.raw().code) &&
            isRetryPolicySetForThisRestCall() &&
            isEndpointRetryable
}
