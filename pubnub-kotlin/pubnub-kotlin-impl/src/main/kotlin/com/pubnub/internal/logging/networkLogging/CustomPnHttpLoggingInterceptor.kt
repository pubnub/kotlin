package com.pubnub.internal.logging.networkLogging

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.HttpMethod
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.managers.MapperManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Base64

private const val PUBNUB_OKHTTP_LOG_TAG = "pubnub.okhttp"

class CustomPnHttpLoggingInterceptor(
    private val logger: PNLogger,
    private val mapperManager: MapperManager,
    private val logVerbosity: PNLogVerbosity,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestStartTime = System.currentTimeMillis()
        val location = request.url.pathSegments.firstOrNull() ?: "unknown"

        // Log request
        logRequest(request, location)

        var response: Response? = null

        try {
            response = chain.proceed(request)

            // Log response (peeks at body without consuming it)
            return logResponse(response, location)
        } catch (e: Exception) {
            logError(request, location, e, requestStartTime)

            throw e
        }
    }

    private fun logRequest(request: Request, location: String) {
        val url = request.url
        val origin = "${url.scheme}://${url.host}"
        val path = url.encodedPath + if (url.encodedQuery != null) {
            "?${url.encodedQuery}"
        } else {
            ""
        }

        // Parse query parameters
        val queryParams = url.queryParameterNames.associateWith { url.queryParameter(it) ?: "" }

        // Parse headers
        val headers = request.headers.toMap()

        val networkRequest = LogMessageContent.NetworkRequest(
            origin = origin,
            path = path,
            query = queryParams.takeIf { it.isNotEmpty() },
            method = HttpMethod.fromString(request.method.lowercase()),
            headers = headers.takeIf { it.isNotEmpty() },
            formData = null, // TODO: Parse form data if needed
            body = request.body.toString(),
            timeout = null, // TODO: Extract from request if available
            identifier = null,
            canceled = false,
            failed = false
        )

        val logMessage = LogMessage(
            message = networkRequest,
            details = "HTTP Request",
            location = location,
        )

        logger.debug(logMessage)
        // to keep PNLogVerbosity.BODY functional
        if (logVerbosity == PNLogVerbosity.BODY) {
            val jsonMessage = mapperManager.toJson(logMessage)
            println("[$PUBNUB_OKHTTP_LOG_TAG] REQUEST: $jsonMessage")
        }
    }

    private fun logResponse(response: Response, location: String): Response {
        val url = response.request.url.toString()
        val status = response.code

        // Parse headers
        val headers = response.headers.toMap()

        // Peek at the body without consuming it (same approach as OkHttp's HttpLoggingInterceptor).
        // This ensures the body remains fully readable by Retrofit for error handling.
        val body: String? = response.body?.let { responseBody ->
            val contentType = responseBody.contentType()?.toString() ?: ""

            try {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body without consuming it.
                val buffer = source.buffer.clone() // Clone for reading.

                if (contentType.contains("application/json") || contentType.startsWith("text/")) {
                    val charset = responseBody.contentType()?.charset() ?: Charsets.UTF_8
                    buffer.readString(charset)
                } else {
                    Base64.getEncoder().encodeToString(buffer.readByteArray())
                }
            } catch (e: IOException) {
                "[Error reading response body: ${e.message}]"
            }
        }

        val logMessage = LogMessage(
            message = LogMessageContent.NetworkResponse(
                url = url,
                status = status,
                headers = headers.takeIf { it.isNotEmpty() },
                body = body
            ),
            details = "HTTP Response",
            location = location,
        )

        logger.debug(logMessage)
        // to keep PNLogVerbosity.BODY functional
        if (logVerbosity == PNLogVerbosity.BODY) {
            val jsonMessage = mapperManager.toJson(logMessage)
            println("[$PUBNUB_OKHTTP_LOG_TAG] RESPONSE: $jsonMessage")
        }

        return response
    }

    private fun logError(request: Request, location: String, error: Exception, requestStartTime: Long) {
        val duration = System.currentTimeMillis() - requestStartTime

        val errorDetails = LogMessageContent.Error(
            type = error.javaClass.simpleName,
            message = error.message ?: "Unknown error",
            stack = error.stackTrace.take(10).map { it.toString() }
        )

        val logMessage = LogMessage(
            message = errorDetails,
            details = "HTTP Request failed after ${duration}ms: ${error.message}",
            location = location,
        )

        // Cancellation is normal Event Engine behavior when there is subscriptionChange
        // Log as debug instead of error to avoid noise in logs
        val isCancellation = error is java.io.IOException && error.message == "Canceled"

        if (isCancellation) {
            logger.debug(logMessage)
            // to keep PNLogVerbosity.BODY functional
            if (logVerbosity == PNLogVerbosity.BODY) {
                val jsonMessage = mapperManager.toJson(logMessage)
                println("[$PUBNUB_OKHTTP_LOG_TAG] DEBUG: $jsonMessage")
            }
        } else {
            logger.error(logMessage)
            // to keep PNLogVerbosity.BODY functional
            if (logVerbosity == PNLogVerbosity.BODY) {
                val jsonMessage = mapperManager.toJson(logMessage)
                println("[$PUBNUB_OKHTTP_LOG_TAG] ERROR: $jsonMessage")
            }
        }
    }
}
