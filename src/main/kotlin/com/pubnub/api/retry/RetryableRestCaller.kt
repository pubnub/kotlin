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

    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)
    private val random = Random.Default
    internal lateinit var call: Call<T>

    internal fun executeRestCallWithRetryPolicy(callToBeExecuted: Call<T>): Response<T> {
        call = callToBeExecuted
        var numberOfAttempts = 0
        while (true) {
            val (response, exception) = executeRestCall()
            if (!shouldRetry(response) || numberOfAttempts++ >= maxRetryNumberFromConfiguration) {
                if (response.isSuccessful || isHttpIssueRepresentedByErrorCodeNotByException(exception)) {
                    return response
                } else {
                    throw exception!!
                }
            }
            val randomDelayInMilliSec = random.nextInt(MAX_RANDOM_DELAY_IN_MILLI_SEC)
            val effectiveDelay = getDelayBasedOnResponse(response).toMillis() + randomDelayInMilliSec
            log.trace("Added random delay so effective retry delay is $effectiveDelay")
            Thread.sleep(effectiveDelay) // we want to sleep here on current thread since this is synchronous call

            call = call.clone()
        }
    }

    private fun isHttpIssueRepresentedByErrorCodeNotByException(exception: Exception?) = exception == null

    private fun executeRestCall(): Pair<Response<T>, Exception?> {
        var pubNubException: Exception? = null
        try {
            return try {
                val response = call.execute()
                Pair(response, pubNubException)
            } catch (e: Exception) {
                pubNubException = PubNubException(
                    pubnubError = PubNubError.PARSING_ERROR,
                    errorMessage = e.toString(),
                    affectedCall = call
                )

                if (isExceptionRetryable(e)) {
                    throw PubNubRetryableException(
                        pubnubError = PubNubError.CONNECT_EXCEPTION,
                        errorMessage = e.toString(),
                        statusCode = SERVICE_UNAVAILABLE // all retryable exceptions internally are mapped to 503 error
                    )
                } else {
                    throw pubNubException
                }
            }
        } catch (e: PubNubRetryableException) {
            return Pair(Response.error(e.statusCode, e.errorMessage?.toResponseBody() ?: "".toResponseBody()), pubNubException)
        }
    }

    private fun isExceptionRetryable(e: Exception): Boolean {
        return retryableExceptions.any { it.isInstance(e) }
    }

    private fun shouldRetry(response: Response<T>) =
        !response.isSuccessful &&
            isErrorCodeRetryable(response.raw().code) &&
            isRetryPolicySetForThisRestCall &&
            isEndpointRetryable
}
