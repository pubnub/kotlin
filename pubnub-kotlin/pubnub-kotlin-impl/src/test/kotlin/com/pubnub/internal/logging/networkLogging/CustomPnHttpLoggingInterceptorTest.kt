package com.pubnub.internal.logging.networkLogging

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.managers.MapperManager
import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Base64

class CustomPnHttpLoggingInterceptorTest {
    private val mapperManager = MapperManager(LogConfig(pnInstanceId = "test-instance", userId = "test-user"))

    /**
     * Captures the last NetworkResponse log body produced when the interceptor processes a response
     * with the given [contentType] and [bodyText].
     */
    private fun loggedResponseBody(contentType: String, bodyText: String): String? {
        var captured: LogMessageContent.NetworkResponse? = null
        val logger = object : PNLogger {
            override fun trace(message: LogMessage) {}

            override fun debug(message: LogMessage) {
                (message.message as? LogMessageContent.NetworkResponse)?.let { captured = it }
            }

            override fun info(message: LogMessage) {}

            override fun warn(message: LogMessage) {}

            override fun error(message: LogMessage) {}

            override fun isDebugEnabled(): Boolean = true
        }

        val interceptor = CustomPnHttpLoggingInterceptor(
            logger = logger,
            mapperManager = mapperManager,
            logVerbosity = PNLogVerbosity.NONE,
            maxLoggedBodyBytes = 1024L,
        )

        val request = Request.Builder().url("https://example.com/v1/datasync/entities").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(bodyText.toResponseBody(contentType.toMediaType()))
            .build()

        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        interceptor.intercept(chain)

        return captured?.body
    }

    @Test
    fun `logs DataSync vendor +json body as readable text, not Base64`() {
        val json = """{"data":{"id":"entity-1","payload":{"hobby":"poetry"}}}"""

        val logged = loggedResponseBody(
            contentType = "application/vnd.pubnub.objects.entity+json;version=1",
            bodyText = json,
        )

        assertEquals(json, logged)
    }

    @Test
    fun `logs plain application-json body as readable text`() {
        val json = """{"status":200,"message":"OK"}"""

        val logged = loggedResponseBody(contentType = "application/json", bodyText = json)

        assertEquals(json, logged)
    }

    @Test
    fun `logs text-slash body as readable text`() {
        val body = """[1,"Sent","17848757453951288"]"""

        val logged = loggedResponseBody(contentType = "text/javascript; charset=UTF-8", bodyText = body)

        assertEquals(body, logged)
    }

    @Test
    fun `Base64-encodes non-JSON binary body`() {
        val body = "not-json-binary-ish"

        val logged = loggedResponseBody(contentType = "application/octet-stream", bodyText = body)

        assertEquals(Base64.getEncoder().encodeToString(body.toByteArray()), logged)
    }
}
