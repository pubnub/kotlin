package com.pubnub.api.legacy.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTestJUnit5
import com.pubnub.api.policies.RequestRetryPolicy
import com.pubnub.api.policies.RetryableEndpointName
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import retrofit2.Call
import retrofit2.Response

class RetryPolicyEndpointTest : BaseTestJUnit5() {

    private fun customCondition(): Boolean {
//        return System.getProperty("os.name").contains("Mac OS X")
        return true // todo change to false so that they will not run
    }

    @Test
    @EnabledIf("customCondition")
    fun `should retry when response is not successful and http error is 429 and endpoint is retryable and endpoint is not excluded in retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Linear(
                    delayInSec = 3, maxRetryNumber = 3, excludedOperations = listOf(RetryableEndpointName.SUBSCRIBE)
                )
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(404, ResponseBody.create(null, "")) // todo change to 429
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint =
            fakeEndpointToTestRetry(call = mockCall, isRetryable = true, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        endpoint.sync()

        // then
        verify(exactly = 4) { mockCall.execute() }
        verify(exactly = 3) { mockCall.clone() }
    }

    @Test
    @EnabledIf("customCondition")
    fun `should not retry when response is not successful and http error is 429 and endpoint is retryable and endpoint is excluded in retryPolicy`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Linear(
                    delayInSec = 3, maxRetryNumber = 3, excludedOperations = listOf(RetryableEndpointName.SUBSCRIBE)
                )
            }
        )
        initPubNub(pubNub)

        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(429, ResponseBody.create(null, ""))
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint =
            fakeEndpointToTestRetry(call = mockCall, isRetryable = true, isEndpointExcludedInRetryPolicy = true)
        endpoint.call = mockCall

        // when
        val exception = Assertions.assertThrows(PubNubException::class.java) {
            endpoint.sync()
        }

        // then
        Assert.assertEquals(429, exception.statusCode)
        verify(exactly = 1) { mockCall.execute() }
        verify(exactly = 0) { mockCall.clone() }
    }

    @Test
    @EnabledIf("customCondition")
    fun `should not retry when response is not successful and http error is 429 and retryPolicy is not configured`() {
        // given
        val mockCall = mockk<Call<Any>>()
        val errorResponse: Response<Any> = Response.error<Any>(429, ResponseBody.create(null, ""))
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall
        val endpoint =
            fakeEndpointToTestRetry(call = mockCall, isRetryable = true, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        val exception = Assertions.assertThrows(PubNubException::class.java) {
            endpoint.sync()
        }

        // then
        Assert.assertEquals(429, exception.statusCode)
        verify(exactly = 1) { mockCall.execute() }
        verify(exactly = 0) { mockCall.clone() }
    }

    @Test
    @EnabledIf("customCondition")
    fun `should not retry when response is not successful and http error is 429 and endpoint is not retryable`() {
        // given
        val pubNub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                newRetryPolicy = RequestRetryPolicy.Linear(
                    delayInSec = 3, maxRetryNumber = 3, excludedOperations = listOf(RetryableEndpointName.SUBSCRIBE)
                )
            }
        )
        initPubNub(pubNub)
        val mockCall = mockk<Call<Any>>()
        every { mockCall.execute() } returns Response.error<Any>(429, ResponseBody.create(null, ""))
        val endpoint =
            fakeEndpointToTestRetry(call = mockCall, isRetryable = false, isEndpointExcludedInRetryPolicy = false)
        endpoint.call = mockCall

        // when
        val exception = Assertions.assertThrows(PubNubException::class.java) {
            endpoint.sync()
        }

        // then
        Assert.assertEquals(429, exception.statusCode)
        verify(exactly = 1) { mockCall.execute() }
        verify(exactly = 0) { mockCall.clone() }
    }

    private fun fakeEndpointToTestRetry(
        call: Call<Any>,
        isRetryable: Boolean,
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
        override fun isEndpointRetryable(): Boolean = isRetryable
        override fun getEndpointName(): RetryableEndpointName? =
            if (isEndpointExcludedInRetryPolicy) null else RetryableEndpointName.FETCH_MESSAGES
    }
}
