package com.pubnub.api.legacy.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTestJUnit5
import com.pubnub.api.policies.RequestRetryPolicy
import com.pubnub.api.policies.RetryableEndpointGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException

private const val RETRY_AFTER_HEADER_NAME = "Retry-After"
private const val RETRY_AFTER_VALUE = "3"
private const val MILLISECONDS = 1000

class RetryPolicyEndpointTest : BaseTestJUnit5() {
    companion object {
        private const val ERROR_CODE_503 = 503
    }

    // this is used for test that execute retryPolicy and thus take more time to execute
    private fun customCondition(): Boolean {
        // return System.getProperty("os.name").contains("Mac OS X")
        return true // todo change to false so that they will not run in GitHub
    }

    @Test
    @EnabledIf("customCondition")
    fun `should retry sync when linear retryPolicy is set and response is not successful and http error is 429 and endpoint is not excluded from retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 3)
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(429, ResponseBody.create(null, ""))
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        endpoint.sync()

        // then
        verify(exactly = 4) { mockCall.execute() }
        verify(exactly = 3) { mockCall.clone() }
    }

    @Test
    @EnabledIf("customCondition")
    fun `should retry sync when linear retryPolicy is set and SocketTimeoutException is thrown and endpoint is not excluded from retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        endpoint.sync()

        // then
        verify(exactly = 3) { mockCall.execute() }
        verify(exactly = 2) { mockCall.clone() }
    }

    @Test
    @EnabledIf("customCondition")
    fun `should retry and fail sync when linear retryPolicy is set and SocketTimeoutException is thrown and endpoint is not excluded from retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThenThrows SocketTimeoutException()
        every { mockCall.clone() } returns mockCall

        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        val exception = assertThrows(PubNubException::class.java) {
            endpoint.sync()
        }

        // then
        assertEquals(ERROR_CODE_503, exception.statusCode)
        verify(exactly = 3) { mockCall.execute() }
        verify(exactly = 3) { mockCall.clone() }
    }

    @Test
    fun `should use Retry-after header from 429 error when value available and linear retryPolicy is set`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)

        val response = mockk<Response<Any>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns RETRY_AFTER_VALUE
        val mockCall = mockk<Call<Any>>()
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThenThrows SocketTimeoutException()
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        val randomDelay = 500

        // when
        val effectiveDelayInMilliSeconds = endpoint.getEffectiveDelayInMilliSeconds(response, retryPolicy, randomDelay)

        assertEquals((RETRY_AFTER_VALUE.toInt() * MILLISECONDS) + randomDelay, effectiveDelayInMilliSeconds)
    }

    @Test
    fun `should not use Retry-after header from 429 error when value is null and linear retryPolicy is set`() {
        // given
        val delayInSec = 2
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = delayInSec, maxRetryNumber = 2)

        val response = mockk<Response<Any>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns null
        val mockCall = mockk<Call<Any>>()
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThenThrows SocketTimeoutException()
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        val randomDelay = 500

        // when
        val effectiveDelayInMilliSeconds = endpoint.getEffectiveDelayInMilliSeconds(response, retryPolicy, randomDelay)

        assertEquals((delayInSec * MILLISECONDS) + randomDelay, effectiveDelayInMilliSeconds)
    }

    @Test
    fun `should not use Retry-after header from 429 error when Retry-after can not be parsed to int value and linear retryPolicy is set`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)

        val response = mockk<Response<Any>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns "20 seconds"
        val mockCall = mockk<Call<Any>>()
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThenThrows SocketTimeoutException()
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        val randomDelay = 500

        // when
        val exception = assertThrows(PubNubException::class.java) {
            endpoint.getEffectiveDelayInMilliSeconds(response, retryPolicy, randomDelay)
        }

        assertEquals(PubNubError.RETRY_AFTER_HEADER_VALUE_CAN_NOT_BE_PARSED_TO_INT.message, exception.errorMessage)
    }

    @Test
    fun `should use maxDelayInSec from Exponential retryPolicy when calculated delay exceeded maxDelayInSec`() {
        // given
        val maxDelayInSec = 5
        val exponentialRetryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = maxDelayInSec,
            maxRetryNumber = 2,
        )
        val mockCall = mockk<Call<Any>>()
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)

        // when
        val firstExponentialDelay = endpoint.getDelay(exponentialRetryPolicy)
        val secondExponentialDelay = endpoint.getDelay(exponentialRetryPolicy)
        val thirdExponentialDelay = endpoint.getDelay(exponentialRetryPolicy)

        // then
        assertEquals(maxDelayInSec, thirdExponentialDelay)
    }

    @Test
    @EnabledIf("customCondition")
    fun `should retry sync when exponential retryPolicy is set and response is not successful and http error is 429 and endpoint is not excluded from retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Exponential(
                    minDelayInSec = 2,
                    maxDelayInSec = 3,
                    maxRetryNumber = 2,
                )
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(429, ResponseBody.create(null, ""))
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        endpoint.sync()

        // then
        verify(exactly = 3) { mockCall.execute() }
        verify(exactly = 2) { mockCall.clone() }
    }

    @Test
    @EnabledIf("customCondition")
    fun `should not retry sync when linear retryPolicy is set and response is not successful and http error is 429 and endpoint is excluded from retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Linear(
                    delayInSec = 2, maxRetryNumber = 2, excludedOperations = listOf(RetryableEndpointGroup.SUBSCRIBE)
                )
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(429, ResponseBody.create(null, ""))
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = true)
        endpoint.call = mockCall

        // when
        val exception = assertThrows(PubNubException::class.java) {
            endpoint.sync()
        }

        // then
        Assert.assertEquals(429, exception.statusCode)
        verify(exactly = 1) { mockCall.execute() }
        verify(exactly = 0) { mockCall.clone() }
    }

    @Test
    @EnabledIf("customCondition")
    fun `should not retry sync when exponential retryPolicy is set and response is not successful and http error is 429 and endpoint is excluded from retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Exponential(
                    minDelayInSec = 2,
                    maxDelayInSec = 3,
                    maxRetryNumber = 2,
                    excludedOperations = listOf(RetryableEndpointGroup.SUBSCRIBE)
                )
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(429, ResponseBody.create(null, ""))
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = true)
        endpoint.call = mockCall

        // when
        val exception = assertThrows(PubNubException::class.java) {
            endpoint.sync()
        }

        // then
        Assert.assertEquals(429, exception.statusCode)
        verify(exactly = 1) { mockCall.execute() }
        verify(exactly = 0) { mockCall.clone() }
    }

    @Test
    fun `should not retry sync when linear retryPolicy is set and response is not successful and http error is 429 and retryPolicy is not configured`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.None
            }
        )
        initPubNub(pubNub)
        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(429, ResponseBody.create(null, ""))
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint = fakeEndpointToTestRetry(call = mockCall, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        val exception = assertThrows(PubNubException::class.java) {
            endpoint.sync()
        }

        // then
        Assert.assertEquals(429, exception.statusCode)
        verify(exactly = 1) { mockCall.execute() }
        verify(exactly = 0) { mockCall.clone() }
    }

    private fun fakeEndpointToTestRetry(
        call: Call<Any>,
        isEndpointExcludedInRetryPolicy: Boolean
    ) = object : Endpoint<Any, Any>(pubnub) {

        override fun doWork(queryParams: HashMap<String, String>): Call<Any> {
            return call
        }

        override fun createResponse(input: Response<Any>) = this
        override fun operationType() = PNOperationType.PNSubscribeOperation
        override fun isSubKeyRequired() = false
        override fun isPubKeyRequired() = false
        override fun isAuthRequired() = false
        override fun getEndpointGroupName(): RetryableEndpointGroup =
            if (isEndpointExcludedInRetryPolicy) RetryableEndpointGroup.SUBSCRIBE else RetryableEndpointGroup.MESSAGE_REACTION
    }
}
