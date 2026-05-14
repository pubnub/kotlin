package com.pubnub.internal.retry

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.models.server.FetchMessagesEnvelope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val RETRY_AFTER_HEADER_NAME = "Retry-After"
private const val RETRY_AFTER_VALUE = "3"

class RetryableRestCallerTest {
    private val testLogConfig = LogConfig("test-instance-id", "test-user-id")

    private fun getRetryableRestCaller(
        retryConfiguration: RetryConfiguration,
        isEndpointRetryable: Boolean = true,
        endpointGroupName: RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE,
    ): RetryableRestCaller<FetchMessagesEnvelope> {
        return RetryableRestCaller(retryConfiguration, endpointGroupName, isEndpointRetryable, testLogConfig)
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
        Assertions.assertEquals(RETRY_AFTER_VALUE.toLong(), delay.inWholeSeconds)
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
        Assertions.assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
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
        Assertions.assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
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
        Assertions.assertEquals(delayInSec.toLong(), delay.inWholeSeconds)
    }

    @Test
    fun `should calculate delay in exponential manner when exponential retryConfiguration is set`() {
        // given
        val retryConfiguration =
            RetryConfiguration.Exponential(
                minDelayInSec = 2,
                maxDelayInSec = 7,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
            )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val delayFromRetryConfiguration01 = retryableRestCaller.getDelayFromRetryConfiguration()
        val delayFromRetryConfiguration02 = retryableRestCaller.getDelayFromRetryConfiguration()
        val delayFromRetryConfiguration03 = retryableRestCaller.getDelayFromRetryConfiguration()
        val delayFromRetryConfiguration04 = retryableRestCaller.getDelayFromRetryConfiguration()

        // then
        Assertions.assertEquals(2, delayFromRetryConfiguration01.inWholeSeconds)
        Assertions.assertEquals(4, delayFromRetryConfiguration02.inWholeSeconds)
        Assertions.assertEquals(7, delayFromRetryConfiguration03.inWholeSeconds)
        Assertions.assertEquals(7, delayFromRetryConfiguration04.inWholeSeconds)
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
        Assertions.assertFalse(retryConfigurationSetForThisRestCall)
    }

    @Test
    fun `when retryConfiguration is Linear and endpoint belong to RetryableEndpointGroup that is excluded from retryConfiguration then restCall is excluded from retry`() {
        // given
        val retryConfiguration =
            RetryConfiguration.Linear(
                delayInSec = 2,
                maxRetryNumber = 2,
                excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE),
            )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val retryConfigurationSetForThisRestCall = retryableRestCaller.isRetryConfSetForThisRestCall

        // then
        Assertions.assertFalse(retryConfigurationSetForThisRestCall)
    }

    @Test
    fun `when retryConfiguration is Exponential and endpoint belong to RetryableEndpointGroup that is excluded from retryConfiguration then restCall is excluded from retry`() {
        // given
        val retryConfiguration =
            RetryConfiguration.Exponential(
                minDelayInSec = 2,
                maxDelayInSec = 7,
                maxRetryNumber = 2,
                excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE),
            )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val retryConfigurationSetForThisRestCall = retryableRestCaller.isRetryConfSetForThisRestCall

        // then
        Assertions.assertFalse(retryConfigurationSetForThisRestCall)
    }

    @Test
    fun `when retryConfiguration is Exponential and endpoint belong to RetryableEndpointGroup that is not excluded from retryConfiguration then restCall is retryable`() {
        // given
        val retryConfiguration =
            RetryConfiguration.Exponential(
                minDelayInSec = 2,
                maxDelayInSec = 7,
                maxRetryNumber = 2,
                excludedOperations = listOf(),
            )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)

        // when
        val retryConfigurationSetForThisRestCall = retryableRestCaller.isRetryConfSetForThisRestCall

        // then
        Assertions.assertTrue(retryConfigurationSetForThisRestCall)
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
        val response = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        Assertions.assertTrue(response.isSuccessful)
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
        val response = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        Assertions.assertFalse(response.isSuccessful)
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
        val response1 = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        Assertions.assertFalse(response1.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and endpoint excluded from retryConfiguration`() {
        // given
        val retryConfiguration =
            RetryConfiguration.Linear(
                delayInSec = 2,
                maxRetryNumber = 2,
                excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE),
            )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(500, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()

        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        Assertions.assertFalse(response1.isSuccessful)
    }

    @Test
    fun `should return unsuccessful response when first attempt failed and endpoint is not retryable`() {
        // given
        val retryConfiguration =
            RetryConfiguration.Linear(
                delayInSec = 2,
                maxRetryNumber = 2,
                excludedOperations = listOf(RetryableEndpointGroup.MESSAGE_PERSISTENCE),
            )
        val retryableRestCaller =
            getRetryableRestCaller(retryConfiguration = retryConfiguration, isEndpointRetryable = false)
        val errorResponse: Response<FetchMessagesEnvelope> = Response.error(500, ResponseBody.create(null, ""))
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        every { mockCall.execute() } returns errorResponse

        // when
        val response1 = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        Assertions.assertFalse(response1.isSuccessful)
    }

    @Test
    fun `should retry successfully when linear retryConfiguration is set and response is not successful and http error is 500 and endpoint is not excluded from retryConfiguration`() {
        // given
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(delayInSec = 10.milliseconds, maxRetryNumber = 3, isInternal = true)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> =
            Response.error<FetchMessagesEnvelope>(500, ResponseBody.create(null, ""))
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 3) { mockCall.clone() }
        verify(exactly = 4) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    @Test
    fun `should retry successfully when exponential retryConfiguration is set and response is not successful and http error is 500 and endpoint is not excluded from retryConfiguration`() {
        // given
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Exponential.createForTest(
                minDelayInSec = 10.milliseconds,
                maxDelayInSec = 15.milliseconds,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
                isInternal = true,
            )
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val errorResponse: Response<FetchMessagesEnvelope> =
            Response.error(500, ResponseBody.create(null, ""))
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } returns errorResponse andThen errorResponse andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 2) { mockCall.clone() }
        verify(exactly = 3) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    @Test
    fun `should retry successfully when linear retryConfiguration is set and SocketTimeoutException is thrown`() {
        // given
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(delayInSec = 10.milliseconds, maxRetryNumber = 2, isInternal = true)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws SocketTimeoutException() andThenThrows SocketTimeoutException() andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        // when
        val response1 = retryableRestCaller.execute(mockCall)

        // then
        verify(exactly = 2) { mockCall.clone() }
        verify(exactly = 3) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    // --- Heartbeat/leave connection-exception classifier tests ---
    //
    // Scope: These tests pin the classifier's behavior for the exception classes that surface on PRESENCE
    // traffic (heartbeat + leave) when a pooled connection is stale after a network switch. They run with
    // PRESENCE NOT in excludedOperations so the retry path is actually exercised — this is the heartbeat
    // shape under a user-opted-in retry config. See the companion test below for default-config behavior.
    @Test
    fun `heartbeat - should retry successfully when SocketException is thrown - stale socket after network switch`() {
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(
                delayInSec = 10.milliseconds,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
                isInternal = true,
            )
        val retryableRestCaller = getRetryableRestCaller(
            retryConfiguration = retryConfiguration,
            endpointGroupName = RetryableEndpointGroup.PRESENCE,
        )
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws SocketException("Socket closed") andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        val response1 = retryableRestCaller.execute(mockCall)

        verify(exactly = 1) { mockCall.clone() }
        verify(exactly = 2) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    @Test
    fun `heartbeat - should retry successfully when SSLException is thrown - stale TLS session after network switch`() {
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(
                delayInSec = 10.milliseconds,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
                isInternal = true,
            )
        val retryableRestCaller = getRetryableRestCaller(
            retryConfiguration = retryConfiguration,
            endpointGroupName = RetryableEndpointGroup.PRESENCE,
        )
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws SSLException("Connection reset") andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        val response1 = retryableRestCaller.execute(mockCall)

        verify(exactly = 1) { mockCall.clone() }
        verify(exactly = 2) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    @Test
    fun `heartbeat - should retry successfully when SSLHandshakeException is thrown`() {
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(
                delayInSec = 10.milliseconds,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
                isInternal = true,
            )
        val retryableRestCaller = getRetryableRestCaller(
            retryConfiguration = retryConfiguration,
            endpointGroupName = RetryableEndpointGroup.PRESENCE,
        )
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws SSLHandshakeException("bad handshake") andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        val response1 = retryableRestCaller.execute(mockCall)

        verify(exactly = 1) { mockCall.clone() }
        verify(exactly = 2) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    @Test
    fun `heartbeat - should retry successfully when ConnectException is thrown`() {
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(
                delayInSec = 10.milliseconds,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
                isInternal = true,
            )
        val retryableRestCaller = getRetryableRestCaller(
            retryConfiguration = retryConfiguration,
            endpointGroupName = RetryableEndpointGroup.PRESENCE,
        )
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws ConnectException("connection refused") andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        val response1 = retryableRestCaller.execute(mockCall)

        verify(exactly = 1) { mockCall.clone() }
        verify(exactly = 2) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    @Test
    fun `heartbeat - should retry successfully on H2 stream reset IOException - H2 stale connection`() {
        // OkHttp's StreamResetException lives in okhttp3.internal.http2 (unstable internal API), but it
        // extends IOException and is the type the SDK would observe. The SDK's retry classifier treats
        // IOException as a retryable exception, so throwing a plain IOException with the message OkHttp
        // would produce ("stream was reset: REFUSED_STREAM") exercises the same code path without
        // depending on internal OkHttp types.
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(
                delayInSec = 10.milliseconds,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
                isInternal = true,
            )
        val retryableRestCaller = getRetryableRestCaller(
            retryConfiguration = retryConfiguration,
            endpointGroupName = RetryableEndpointGroup.PRESENCE,
        )
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws IOException("stream was reset: REFUSED_STREAM") andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        val response1 = retryableRestCaller.execute(mockCall)

        verify(exactly = 1) { mockCall.clone() }
        verify(exactly = 2) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    @Test
    fun `heartbeat - should retry successfully when IOException 'unexpected end of stream' is thrown`() {
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(
                delayInSec = 10.milliseconds,
                maxRetryNumber = 2,
                excludedOperations = emptyList(),
                isInternal = true,
            )
        val retryableRestCaller = getRetryableRestCaller(
            retryConfiguration = retryConfiguration,
            endpointGroupName = RetryableEndpointGroup.PRESENCE,
        )
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        val successfulResponse: Response<FetchMessagesEnvelope> = Response.success(null)
        every { mockCall.execute() } throws IOException("unexpected end of stream") andThen successfulResponse
        every { mockCall.clone() } returns mockCall

        val response1 = retryableRestCaller.execute(mockCall)

        verify(exactly = 1) { mockCall.clone() }
        verify(exactly = 2) { mockCall.execute() }
        Assertions.assertTrue(response1.isSuccessful)
    }

    // --- Characterization test: PRESENCE-excluded RetryConfiguration does not retry heartbeats ---
    //
    // Describes the behavior of any RetryConfiguration that has PRESENCE in excludedOperations: a
    // stale-connection IOException from a network switch propagates unretried, so the user sees a
    // failed heartbeat on the first post-switch tick. This test fabricates such a config rather than
    // reading it from PNConfigurationImpl — at time of writing the SDK's default
    // RetryConfiguration.Exponential also excludes PRESENCE (see PNConfigurationImpl.retryConfiguration),
    // so this test is representative of the default-path behavior, but it does not anchor on the
    // default: if someone changes that list, this test will not catch it. Flip the behavior by either
    // (a) opting in to a retry config that does not exclude PRESENCE, or (b) changing the SDK default
    // (separate discussion).
    @Test
    fun `heartbeat does NOT retry on stale connection when PRESENCE is excluded from retries`() {
        val retryConfiguration =
            RetryConfiguration.Exponential(
                minDelayInSec = 2,
                maxDelayInSec = 2,
                maxRetryNumber = 2,
                excludedOperations = listOf(RetryableEndpointGroup.PRESENCE),
            )
        val retryableRestCaller = getRetryableRestCaller(
            retryConfiguration = retryConfiguration,
            endpointGroupName = RetryableEndpointGroup.PRESENCE,
        )
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        every { mockCall.execute() } throws IOException("stream was reset: REFUSED_STREAM")

        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                retryableRestCaller.execute(mockCall)
            }

        verify(exactly = 0) { mockCall.clone() }
        verify(exactly = 1) { mockCall.execute() }
        Assertions.assertEquals(PubNubError.PARSING_ERROR, exception.pubnubError)
    }

    @Test
    fun `should retry and throw exception when linear retryConfiguration is set and UnknownHostException is thrown`() {
        // given
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(delayInSec = 10.milliseconds, maxRetryNumber = 2, isInternal = true)
        val retryableRestCaller = getRetryableRestCaller(retryConfiguration = retryConfiguration)
        val mockCall = mockk<Call<FetchMessagesEnvelope>>()
        every { mockCall.execute() } throws UnknownHostException() andThenThrows UnknownHostException() andThenThrows UnknownHostException()
        every { mockCall.clone() } returns mockCall

        // when
        val exception =
            Assertions.assertThrows(PubNubException::class.java) {
                retryableRestCaller.execute(mockCall)
            }

        // then
        verify(exactly = 2) { mockCall.clone() }
        verify(exactly = 3) { mockCall.execute() }
        Assertions.assertEquals("java.net.UnknownHostException", exception.errorMessage)
        Assertions.assertEquals(PubNubError.PARSING_ERROR, exception.pubnubError)
    }
}
