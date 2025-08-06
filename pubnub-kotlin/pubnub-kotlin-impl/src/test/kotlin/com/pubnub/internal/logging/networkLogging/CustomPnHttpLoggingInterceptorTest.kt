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
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.event.Level

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
}
