package com.pubnub.api.retry

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.models.server.FetchMessagesEnvelope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
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
        retryConfiguration: RetryConfiguration,
        isEndpointRetryable: Boolean = true,
        endpointGroupName: RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE
    ): RetryableRestCaller<FetchMessagesEnvelope> {
        return RetryableRestCaller(retryConfiguration, endpointGroupName, isEndpointRetryable)
    }

    @Test
    fun `should use Retry-after header from 429 error when value available and linear retryConfiguration is set`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns RETRY_AFTER_VALUE

        // when
        val delay: Duration = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(RETRY_AFTER_VALUE.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should use delay from retryConfiguration when value of Retry-after header from 429 error is null and linear retryConfiguration is set`() {
        // given
        val delayInSec = 2
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = delayInSec, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns null
        // when
        val delay = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should use delay from retryConfiguration when error is nt 429 and linear retryConfiguration is set`() {
        // given
        val delayInSec = 2
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = delayInSec, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 500
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns null
        // when
        val delay = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should use delay from retryConfiguration when value of Retry-after header from 429 error can not be parsed to int and linear retryConfiguration is set`() {
        // given
        val delayInSec = 2
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = delayInSec, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        val response = mockk<Response<FetchMessagesEnvelope>>()
        every { response.raw().code } returns 429
        every { response.headers()[RETRY_AFTER_HEADER_NAME] } returns "20 seconds"

        // when
        val delay = retryableRestCaller.getDelayBasedOnResponse(response)

        // then
        assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should calculate delay in exponential manner when exponential retryConfiguration is set`() {
        // given
        val retryConfiguration = RetryConfiguration.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 7,
            maxRetryNumber = 2,
            excludedOperations = emptyList()
        )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val delayFromRetryConfiguration01 = retryableRestCaller.getDelayFromRetryConfiguration()
        val delayFromRetryConfiguration02 = retryableRestCaller.getDelayFromRetryConfiguration()
        val delayFromRetryConfiguration03 = retryableRestCaller.getDelayFromRetryConfiguration()
        val delayFromRetryConfiguration04 = retryableRestCaller.getDelayFromRetryConfiguration()

        // then
        assertEquals(2, delayFromRetryConfiguration01)
        assertEquals(4, delayFromRetryConfiguration02)
        assertEquals(7, delayFromRetryConfiguration03)
        assertEquals(7, delayFromRetryConfiguration04)
    }

    @Test
    fun `when retryConfiguration is None then each restCall is excluded from retry`() {
        // given
        val retryConfiguration = RetryConfiguration.None
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val retryConfigurationSetForThisRestCall =
            retryableRestCaller.isRetryConfSetForThisRestCall

        // then
        assertFalse(retryConfigurationSetForThisRestCall)
    }

    @Test
    fun `when retryConfiguration is Linear and endpoint belong to RetryableEndpointGroup that is excluded from retryConfiguration then restCall is excluded from retry`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(
            delayInSec = 2,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val retryConfigurationSetForThisRestCall = retryableRestCaller.isRetryConfSetForThisRestCall

        // then
        assertFalse(retryConfigurationSetForThisRestCall)
    }

    @Test
    fun `when retryConfiguration is Exponential and endpoint belong to RetryableEndpointGroup that is excluded from retryConfiguration then restCall is excluded from retry`() {
        // given
        val retryConfiguration = RetryConfiguration.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 7,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val retryConfigurationSetForThisRestCall = retryableRestCaller.isRetryConfSetForThisRestCall

        // then
        assertFalse(retryConfigurationSetForThisRestCall)
    }

    @Test
    fun `when retryConfiguration is Exponential and endpoint belong to RetryableEndpointGroup that is not excluded from retryConfiguration then restCall is retryable`() {
        // given
        val retryConfiguration = RetryConfiguration.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 7,
            maxRetryNumber = 2,
            excludedOperations = listOf()
        )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val retryConfigurationSetForThisRestCall = retryableRestCaller.isRetryConfSetForThisRestCall

        // then
        assertTrue(retryConfigurationSetForThisRestCall)
    }

    @Test
    fun `should execute rest call once when first attempt successful`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns successfulResponse

        // when
        val response = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertTrue(response.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and retryConfiguration not set`() {
        // given
        val retryConfiguration = RetryConfiguration.None
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(429, ResponseBody.create(null, ""))
        every { mockCall.execute() } returns errorResponse

        // when
        val response = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and retryConfiguration set and error code is not retryable`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(404, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()

        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response1.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and endpoint excluded from retryConfiguration`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(
            delayInSec = 2,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(500, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()

        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response1.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and endpoint is not retryable`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(
            delayInSec = 2,
            maxRetryNumber = 2,
            excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE)
        )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration, isEndpointRetryable = false)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(500, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        assertFalse(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry successfully when linear retryConfiguration is set and response is not successful and http error is 500 and endpoint is not excluded from retryConfiguration`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 2, maxRetryNumber = 3)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> =
            Response.error<FetchMessagesEnvelope>(500, ResponseBody.create(null, ""))
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 3) { mockCall.clone() }
        verify(exactly = 4) { mockCall.execute() }
        assertTrue(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry successfully when exponential retryConfiguration is set and response is not successful and http error is 500 and endpoint is not excluded from retryConfiguration`() {
        // given
        val retryConfiguration = RetryConfiguration.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 3,
            maxRetryNumber = 2,
            excludedOperations = emptyList()
        )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> =
            Response.error(500, ResponseBody.create(null, ""))
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 2) { mockCall.clone() }
        verify(exactly = 3) { mockCall.execute() }
        assertTrue(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry successfully when linear retryConfiguration is set and SocketTimeoutException is thrown`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.executeRestCallWithRetryConf(mockCall)

        // then
        verify(exactly = 2) { mockCall.clone() }
        verify(exactly = 3) { mockCall.execute() }
        assertTrue(response1.isSuccessful)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry and throw exception when linear retryConfiguration is set and UnknownHostException is thrown`() {
        // given
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 2, maxRetryNumber = 2)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        every { mockCall.execute() } throws UnknownHostException() andThenThrows UnknownHostException() andThenThrows UnknownHostException()
        every { mockCall.clone() } returns mockCall

        // when
        val exception = assertThrows(PubNubException::class.java) {
            retryableRestCaller.executeRestCallWithRetryConf(mockCall)
        }

        // then
        verify(exactly = 2) { mockCall.clone() }
        verify(exactly = 3) { mockCall.execute() }
        assertEquals("java.net.UnknownHostException", exception.errorMessage)
        assertEquals(PubNubError.PARSING_ERROR, exception.pubnubError)
    }
}
