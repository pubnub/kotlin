package com.pubnub.api.retry

import com.pubnub.api.models.server.FetchMessagesEnvelope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.time.Duration

private const val RETRY_AFTER_HEADER_NAME = "Retry-After"
private const val RETRY_AFTER_VALUE = "3"

class RetryableRestCallerTest : RetryableTestBase() {
    private fun getRetryableRestCaller(
        retryPolicy: RequestRetryPolicy,
        isEndpointRetryable: Boolean = true,
        endpointGroupName: RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE
    ): RetryableRestCaller<FetchMessagesEnvelope> {
        return RetryableRestCaller(retryPolicy, endpointGroupName, isEndpointRetryable)
    }

    @Test
    fun `should use Retry-after header from 429 error when value available and linear retryPolicy is set`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns RETRY_AFTER_VALUE

        // when
        val delay: Duration = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(RETRY_AFTER_VALUE.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should use delay from retryPolicy when value of Retry-after header from 429 error is null and linear retryPolicy is set`() {
        // given
        val delayInSec = 2
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = delayInSec, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns null
        // when
        val delay = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should use delay from retryPolicy when error is nt 429 and linear retryPolicy is set`() {
        // given
        val delayInSec = 2
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = delayInSec, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 500
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns null
        // when
        val delay = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should use delay from retryPolicy when value of Retry-after header from 429 error can not be parsed to int and linear retryPolicy is set`() {
        // given
        val delayInSec = 2
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = delayInSec, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns "20 seconds"

        // when
        val delay = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should calculate delay in exponential manner when exponential retryPolicy is set`() {
        // given
        val retryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 7,
            maxRetryNumber = 2,
            excludedOperations = emptyList()
        )
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        // when
        val delayFromRetryPolicy01 = retryableRestCaller.getDelayFromRetryPolicy()
        val delayFromRetryPolicy02 = retryableRestCaller.getDelayFromRetryPolicy()
        val delayFromRetryPolicy03 = retryableRestCaller.getDelayFromRetryPolicy()
        val delayFromRetryPolicy04 = retryableRestCaller.getDelayFromRetryPolicy()

        // then
        assertEquals(2, delayFromRetryPolicy01)
        assertEquals(4, delayFromRetryPolicy02)
        assertEquals(7, delayFromRetryPolicy03)
        assertEquals(7, delayFromRetryPolicy04)
    }

    @Test
    fun `when retryPolicy is None then each restCall is excluded from retry`() {
        // given
        val retryPolicy = RequestRetryPolicy.None
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        // when
        val retryPolicySetForThisRestCall =
            retryableRestCaller.isRetryPolicySetForThisRestCall

        // then
        assertFalse(retryPolicySetForThisRestCall)
    }

    @Test
    fun `when retryPolicy is Linear and endpoint belong to RetryableEndpointGroup that is excluded from retryPolicy then restCall is excluded from retry`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(
            delayInSec = 2,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        // when
        val retryPolicySetForThisRestCall = retryableRestCaller.isRetryPolicySetForThisRestCall

        // then
        assertFalse(retryPolicySetForThisRestCall)
    }

    @Test
    fun `when retryPolicy is Exponential and endpoint belong to RetryableEndpointGroup that is excluded from retryPolicy then restCall is excluded from retry`() {
        // given
        val retryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 7,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        // when
        val retryPolicySetForThisRestCall = retryableRestCaller.isRetryPolicySetForThisRestCall

        // then
        assertFalse(retryPolicySetForThisRestCall)
    }

    @Test
    fun `when retryPolicy is Exponential and endpoint belong to RetryableEndpointGroup that is not excluded from retryPolicy then restCall is retryable`() {
        // given
        val retryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 7,
            maxRetryNumber = 2,
            excludedOperations = listOf()
        )
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)

        // when
        val retryPolicySetForThisRestCall = retryableRestCaller.isRetryPolicySetForThisRestCall

        // then
        assertTrue(retryPolicySetForThisRestCall)
    }

    @Test
    fun `should execute rest call once when first attempt successful`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns successfulResponse

        // when
        val response = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertTrue(response.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and retryPolicy not set`() {
        // given
        val retryPolicy = RequestRetryPolicy.None
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(429, ResponseBody.create(null, ""))
        every { mockCall.execute() } returns errorResponse

        // when
        val response = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and retryPolicy set and error code is not retryable`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(404, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()

        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response1.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and endpoint excluded from retryPolicy`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(
            delayInSec = 2,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryPolicy)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(500, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()

        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response1.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and endpoint is not retryable`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(
            delayInSec = 2,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryPolicy = retryPolicy, isEndpointRetryable = false)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(500, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry successfully when linear retryPolicy is set and response is not successful and http error is 500 and endpoint is not excluded from retryPolicy`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy = retryPolicy)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> =
            Response.error<FetchMessagesEnvelope>(500, ResponseBody.create(null, ""))
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 3) { mockCall.clone() }
        verify(exactly = 4) { mockCall.execute() }
        assertTrue(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry successfully when exponential retryPolicy is set and response is not successful and http error is 500 and endpoint is not excluded from retryPolicy`() {
        // given
        val retryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 3,
            maxRetryNumber = 2,
            excludedOperations = emptyList()
        )
        val retryableRestCaller = getRetryableRestCaller(retryPolicy = retryPolicy)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> =
            Response.error(500, ResponseBody.create(null, ""))
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 3) { mockCall.clone() }
        verify(exactly = 4) { mockCall.execute() }
        assertTrue(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry successfully when linear retryPolicy is set and SocketTimeoutException is thrown`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy = retryPolicy)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 2) { mockCall.clone() }
        verify(exactly = 3) { mockCall.execute() }
        assertTrue(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry and fail when linear retryPolicy is set and UnknownHostException is thrown`() {
        // given
        val retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryPolicy = retryPolicy)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        every { mockCall.execute() } throws UnknownHostException() andThenThrows UnknownHostException() andThenThrows UnknownHostException()
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryPolicy(mockCall)

        // then
        verify(exactly = 3) { mockCall.clone() }
        verify(exactly = 4) { mockCall.execute() }
        assertFalse(response1.isSuccessful)
    }
}
