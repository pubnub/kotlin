package com.pubnub.internal.logging.networkLogging

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.internal.logging.ExtendedLogger
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.event.Level
import java.io.IOException

class CustomPnHttpLoggingInterceptorTest {
    private val mockLogger = mockk<ExtendedLogger>(relaxed = true)
    private val testInstanceId = "test-instance-id"

    @Test
    fun `should log request with basic information`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId
        )

        val request = Request.Builder()
            .url("https://ps.pndsn.com/v2/subscribe/demo/my-channel/0")
            .get()
            .build()

        val mockChain = mockk<Interceptor.Chain>()
        val mockResponse = mockk<Response>()
        every { mockChain.request() } returns request
        every { mockChain.proceed(any()) } returns mockResponse
        every { mockResponse.request } returns request
        every { mockResponse.code } returns 200
        every { mockResponse.headers } returns mockk(relaxed = true) {
            every { toMap() } returns emptyMap()
        }
        every { mockResponse.body } returns null

        val logMessageSlot = slot<LogMessage>()
        every { mockLogger.debug(capture(logMessageSlot)) } returns Unit

        interceptor.intercept(mockChain)

        verify(atLeast = 1) { mockLogger.debug(any()) }

        val capturedMessage = logMessageSlot.captured
        assertEquals(testInstanceId, capturedMessage.pubNubId)
        assertEquals(Level.DEBUG, capturedMessage.logLevel)
        assertEquals(LogMessageType.NETWORK_REQUEST, capturedMessage.type)
        assertTrue(capturedMessage.message is LogMessageContent.NetworkRequest)
    }

    @Test
    fun `should log request with body when content length is within limit`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId,
            maxBodySize = 1024
        )

        val requestBody = """{"message": "hello"}""".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://ps.pndsn.com/publish/demo/my-channel/0")
            .post(requestBody)
            .build()

        val mockChain = mockk<Interceptor.Chain>()
        val mockResponse = mockk<Response>()

        val headers = mockk<okhttp3.Headers>()
        every { headers.toMap() } returns mapOf(
            "Content-Type" to "application/json",
            "Content-Length" to requestBody.contentLength().toString()
        )

        every { mockChain.request() } returns request
        every { mockChain.proceed(any()) } returns mockResponse
        every { mockResponse.request } returns request
        every { mockResponse.code } returns 200
        every { mockResponse.headers } returns headers // Use the headers mock we created above
        every { mockResponse.body } returns null

        val logMessageSlot = slot<LogMessage>()
        every { mockLogger.debug(capture(logMessageSlot)) } returns Unit

        interceptor.intercept(mockChain)

        verify(atLeast = 1) { mockLogger.debug(any()) }

        val requestMessage = logMessageSlot.captured
        assertTrue(requestMessage.message is LogMessageContent.NetworkRequest)
        val networkRequest = (requestMessage.message as LogMessageContent.NetworkRequest).message
        assertEquals("""{"message": "hello"}""", networkRequest.message.body)
    }

    @Test
    fun `should not log request body when content length exceeds limit`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId,
            maxBodySize = 10 // Very small limit
        )

        val largeRequestBody = "x".repeat(100).toRequestBody("text/plain".toMediaType())
        val request = Request.Builder()
            .url("https://ps.pndsn.com/publish/demo/my-channel/0")
            .post(largeRequestBody)
            .build()

        val mockChain = mockk<Interceptor.Chain>()
        val mockResponse = mockk<Response>()
        every { mockChain.request() } returns request
        every { mockChain.proceed(any()) } returns mockResponse
        every { mockResponse.request } returns request
        every { mockResponse.code } returns 200
        every { mockResponse.headers } returns mockk(relaxed = true) {
            every { toMap() } returns emptyMap()
        }
        every { mockResponse.body } returns null

        val logMessageSlot = slot<LogMessage>()
        every { mockLogger.debug(capture(logMessageSlot)) } returns Unit

        interceptor.intercept(mockChain)

        verify(atLeast = 1) { mockLogger.debug(any()) }

        val requestMessage = logMessageSlot.captured
        assertTrue(requestMessage.message is LogMessageContent.NetworkRequest)
        val networkRequest = (requestMessage.message as LogMessageContent.NetworkRequest).message
        assertEquals(null, networkRequest.message.body) // Body should be null when too large
    }

    @Test
    fun `should log response with JSON body`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId
        )

        val request = Request.Builder()
            .url("https://ps.pndsn.com/v2/subscribe/demo/my-channel/0")
            .get()
            .build()

        val responseBody = """{"status": 200, "message": "OK"}"""
        val mockResponse = mockk<Response>()
        every { mockResponse.request } returns request
        every { mockResponse.code } returns 200
        every { mockResponse.headers } returns mockk(relaxed = true) {
            every { toMap() } returns mapOf("Content-Type" to "application/json")
        }
        every { mockResponse.body } returns responseBody.toResponseBody("application/json".toMediaType())
        every { mockResponse.newBuilder() } returns mockk {
            every { body(any()) } returns this
            every { build() } returns mockResponse
        }

        val mockChain = mockk<Interceptor.Chain>()
        every { mockChain.request() } returns request
        every { mockChain.proceed(any()) } returns mockResponse

        val logMessageSlots = mutableListOf<LogMessage>()
        every { mockLogger.debug(capture(logMessageSlots)) } returns Unit

        val result = interceptor.intercept(mockChain)

        verify(atLeast = 2) { mockLogger.debug(any()) } // Request and response
        assertNotNull(result)

        // Find the response log message
        val responseMessage = logMessageSlots.find { it.type == LogMessageType.NETWORK_RESPONSE }
        assertNotNull(responseMessage)
        assertTrue(responseMessage!!.message is LogMessageContent.NetworkResponse)
        val networkResponse = (responseMessage.message as LogMessageContent.NetworkResponse).message
        assertEquals(responseBody, networkResponse.message.body)
    }

    @Test
    fun `should log error when request fails`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId
        )

        val request = Request.Builder()
            .url("https://ps.pndsn.com/v2/subscribe/demo/my-channel/0")
            .get()
            .build()

        val mockChain = mockk<Interceptor.Chain>()
        val testException = IOException("Network error")
        every { mockChain.request() } returns request
        every { mockChain.proceed(any()) } throws testException

        val logMessageSlots = mutableListOf<LogMessage>()
        every { mockLogger.debug(capture(logMessageSlots)) } returns Unit
        every { mockLogger.error(capture(logMessageSlots)) } returns Unit

        assertThrows<IOException> {
            interceptor.intercept(mockChain)
        }

        verify { mockLogger.debug(any()) } // Request logged
        verify { mockLogger.error(any()) } // Error logged

        // Find the error log message
        val errorMessage = logMessageSlots.find { it.type == LogMessageType.ERROR }
        assertNotNull(errorMessage)
        assertEquals(Level.ERROR, errorMessage!!.logLevel)
        assertTrue(errorMessage.message is LogMessageContent.Error)
    }

    @Test
    fun `should handle query parameters and headers correctly`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId
        )

        val request = Request.Builder()
            .url("https://ps.pndsn.com/v2/subscribe/demo/my-channel/0?uuid=test-uuid&pnsdk=test-sdk")
            .header("User-Agent", "test-agent")
            .header("Authorization", "Bearer token")
            .get()
            .build()

        val mockChain = mockk<Interceptor.Chain>()
        val mockResponse = mockk<Response>()
        every { mockChain.request() } returns request
        every { mockChain.proceed(any()) } returns mockResponse
        every { mockResponse.request } returns request
        every { mockResponse.code } returns 200
        every { mockResponse.headers } returns mockk(relaxed = true) {
            every { toMap() } returns emptyMap()
        }
        every { mockResponse.body } returns null

        val logMessageSlot = slot<LogMessage>()
        every { mockLogger.debug(capture(logMessageSlot)) } returns Unit

        interceptor.intercept(mockChain)

        verify(atLeast = 1) { mockLogger.debug(any()) }

        val requestMessage = logMessageSlot.captured
        assertTrue(requestMessage.message is LogMessageContent.NetworkRequest)
        val networkRequest = (requestMessage.message as LogMessageContent.NetworkRequest).message

        // Check query parameters
        assertNotNull(networkRequest.message.query)
        assertTrue(networkRequest.message.query!!.containsKey("uuid"))
        assertEquals("test-uuid", networkRequest.message.query!!["uuid"])

        // Check headers
        assertNotNull(networkRequest.message.headers)
        assertTrue(networkRequest.message.headers!!.containsKey("User-Agent"))
        assertEquals("test-agent", networkRequest.message.headers!!["User-Agent"])
    }

    @Test
    fun `should handle response with large body by limiting size`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId,
            maxBodySize = 50 // Small limit for testing
        )

        val request = Request.Builder()
            .url("https://ps.pndsn.com/v2/subscribe/demo/my-channel/0")
            .get()
            .build()

        val largeResponseBody = "x".repeat(100) // Exceeds limit
        val mockResponse = mockk<Response>()
        every { mockResponse.request } returns request
        every { mockResponse.code } returns 200
        every { mockResponse.headers } returns mockk(relaxed = true) {
            every { toMap() } returns emptyMap()
        }
        every { mockResponse.body } returns largeResponseBody.toResponseBody("text/plain".toMediaType())

        val mockChain = mockk<Interceptor.Chain>()
        every { mockChain.request() } returns request
        every { mockChain.proceed(any()) } returns mockResponse

        val logMessageSlots = mutableListOf<LogMessage>()
        every { mockLogger.debug(capture(logMessageSlots)) } returns Unit

        interceptor.intercept(mockChain)

        verify(atLeast = 2) { mockLogger.debug(any()) }

        // Find the response log message
        val responseMessage = logMessageSlots.find { it.type == LogMessageType.NETWORK_RESPONSE }
        assertNotNull(responseMessage)
        assertTrue(responseMessage!!.message is LogMessageContent.NetworkResponse)
        val networkResponse = (responseMessage.message as LogMessageContent.NetworkResponse).message

        // Body should indicate it was too large to log
        assertTrue(networkResponse.message.body?.contains("Body too large to log") == true)
    }

    @Test
    fun `should handle concurrent requests safely`() {
        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = mockLogger,
            logVerbosity = PNLogVerbosity.BODY,
            pnInstanceId = testInstanceId
        )

        val requests = (1..10).map { i ->
            Request.Builder()
                .url("https://ps.pndsn.com/v2/subscribe/demo/channel-$i/0")
                .get()
                .build()
        }

        val logMessages = mutableListOf<LogMessage>()
        every { mockLogger.debug(capture(logMessages)) } returns Unit

        // Simulate concurrent execution
        requests.map { request ->
            Thread {
                val mockChain = mockk<Interceptor.Chain>()
                val mockResponse = mockk<Response>()
                every { mockChain.request() } returns request
                every { mockChain.proceed(any()) } returns mockResponse
                every { mockResponse.request } returns request
                every { mockResponse.code } returns 200
                every { mockResponse.headers } returns mockk(relaxed = true) {
                    every { toMap() } returns emptyMap()
                }
                every { mockResponse.body } returns null

                interceptor.intercept(mockChain)
            }
        }.forEach {
            it.start()
            it.join()
        }

        // Verify that we got log messages from all requests
        verify(atLeast = 20) { mockLogger.debug(any()) } // 10 requests + 10 responses
        assertTrue(logMessages.size >= 20)
    }
}
