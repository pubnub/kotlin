package com.pubnub.internal.logging.networkLogging

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.logging.ExtendedLogger
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.logging.NetworkLog
import com.pubnub.internal.managers.MapperManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import org.slf4j.helpers.NOPLoggerFactory
import java.io.IOException
import java.util.Base64

private const val PUBNUB_OKHTTP_REQUEST_RESPONSE_LOGGER_NAME = "pubnub.okhttp"

class CustomHttpLoggingInterceptor(
    private val logger: ExtendedLogger,
    private val logVerbosity: PNLogVerbosity,
    private val maxBodySize: Int = 1024 * 1024 // 1MB limit  todo clarify in ADR
) : Interceptor {

    private fun slf4jIsBound(): Boolean {
        val factory = LoggerFactory.getILoggerFactory()
        return factory !is NOPLoggerFactory
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestStartTime = System.currentTimeMillis()

        // Log request
        logRequest(request)

        var response: Response? = null
        var failed = false
        var canceled = false

        try {
            response = chain.proceed(request)

            // Log response and get the modified response with cloned body
            val modifiedResponse = logResponse(response, requestStartTime, failed, canceled)

            return modifiedResponse
        } catch (e: Exception) {
            failed = true
            throw e
        }
    }

    private fun logRequest(request: Request) {
        val url = request.url
        val origin = "${url.scheme}://${url.host}"
        val path = url.encodedPath + if (url.encodedQuery != null) "?${url.encodedQuery}" else ""

        // Parse query parameters
        val queryParams = url.queryParameterNames.associateWith { url.queryParameter(it) ?: "" }

        // Parse headers
        val headers = request.headers.toMap()

        // Parse body
        val body = request.body?.let { requestBody ->
            if (requestBody.contentLength() > 0 && requestBody.contentLength() <= maxBodySize) {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                buffer.readUtf8()
            } else null
        }

        val networkRequest = NetworkRequestMessage(
            origin = origin,
            path = path,
            query = queryParams.takeIf { it.isNotEmpty() },
            method = HttpMethod.fromString(request.method.lowercase()),
            headers = headers.takeIf { it.isNotEmpty() },
            formData = null, // TODO: Parse form data if needed
            body = body,
            timeout = null, // TODO: Extract from request if available
            identifier = queryParams["requestid"]
        )

        val requestLog = NetworkLog.Request(
            message = networkRequest,
            canceled = false,
            failed = false
        )

        val logMessage = LogMessage(
            timestamp = System.currentTimeMillis(),
            pubNubId = "TODO", // You'll need to get this from context
            logLevel = Level.DEBUG,
            location = "CustomHttpLoggingInterceptor",
            type = LogMessageType.NETWORK_REQUEST,
            message = LogMessageContent.NetworkRequest(requestLog),
            details = "HTTP Request"
        )


        if (slf4jIsBound()) {
            logger.debug(logMessage)
        } else {
            // fallback: always print
            if (logVerbosity == PNLogVerbosity.BODY) {
                val jsonMessage = MapperManager().toJson(logMessage)
                println("[$PUBNUB_OKHTTP_REQUEST_RESPONSE_LOGGER_NAME] REQUEST: $jsonMessage")
            }
        }
    }

    private fun logResponse(response: Response, requestStartTime: Long, failed: Boolean, canceled: Boolean): Response {
        val url = response.request.url.toString()
        val status = response.code
        val duration = System.currentTimeMillis() - requestStartTime

        // Parse headers
        val headers = response.headers.toMap()

        // Parse body and create modified response with cloned body
        val (body, modifiedResponse) = response.body?.let { responseBody ->
            val contentLength = responseBody.contentLength()
            if (contentLength > 0 && contentLength <= maxBodySize) {
                val contentType = responseBody.contentType()?.toString() ?: ""

                try {
                    when {
                        contentType.contains("application/json") -> {
                            val bodyString = responseBody.string()
                            val clonedResponseBody = ResponseBody.create(responseBody.contentType(), bodyString)
                            val newResponse = response.newBuilder().body(clonedResponseBody).build()
                            bodyString to newResponse
                        }
                        contentType.startsWith("text/") -> {
                            val bodyString = responseBody.string()
                            val clonedResponseBody = ResponseBody.create(responseBody.contentType(), bodyString)
                            val newResponse = response.newBuilder().body(clonedResponseBody).build()
                            bodyString to newResponse
                        }
                        else -> {
                            val bytes = responseBody.bytes()
                            val clonedResponseBody = ResponseBody.create(responseBody.contentType(), bytes)
                            val newResponse = response.newBuilder().body(clonedResponseBody).build()
                            Base64.getEncoder().encodeToString(bytes) to newResponse
                        }
                    }
                } catch (e: IOException) {
                    "[Error reading response body: ${e.message}]" to response
                }
            } else if (contentLength > maxBodySize) {
                "[Body too large to log: $contentLength bytes (max: $maxBodySize bytes)]" to response
            } else {
                null to response
            }
        } ?: (null to response)

        val networkResponse = NetworkResponseMessage(
            url = url,
            status = status,
            headers = headers.takeIf { it.isNotEmpty() },
            body = body
        )

        val responseLog = NetworkLog.Response(message = networkResponse)

        val logMessage = LogMessage(
            timestamp = System.currentTimeMillis(),
            pubNubId = "TODO", // You'll need to get this from context
            logLevel = Level.DEBUG,
            location = "CustomHttpLoggingInterceptor",
            type = LogMessageType.NETWORK_RESPONSE,
            message = LogMessageContent.NetworkResponse(responseLog),
            details = "HTTP Response"
        )

        if (slf4jIsBound()) {
            logger.debug(logMessage)
        } else {
            // fallback: always print
            if (logVerbosity == PNLogVerbosity.BODY) {
                val jsonMessage = MapperManager().toJson(logMessage)
                println("[$PUBNUB_OKHTTP_REQUEST_RESPONSE_LOGGER_NAME] RESPONSE: $jsonMessage")
            }
        }

        return modifiedResponse
    }
}
