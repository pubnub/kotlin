package com.pubnub.internal.logging.networkLogging

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.ErrorDetails
import com.pubnub.api.logging.HttpMethod
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.NetworkLog
import com.pubnub.api.logging.NetworkRequestMessage
import com.pubnub.api.logging.NetworkResponseMessage
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.managers.MapperManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.util.Base64

private const val PUBNUB_OKHTTP_LOG_TAG = "pubnub.okhttp"

class CustomPnHttpLoggingInterceptor(
    private val logger: PNLogger,
    private val logVerbosity: PNLogVerbosity,
    private val pnInstanceId: String,
) : Interceptor {
    private val mapperManager = MapperManager()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestStartTime = System.currentTimeMillis()
        val location = request.url.pathSegments.firstOrNull() ?: "unknown"

        // Log request
        logRequest(request, location)

        var response: Response? = null

        try {
            response = chain.proceed(request)

            // Log response and get the modified response with cloned body
            val modifiedResponse = logResponse(response, location)
            return modifiedResponse
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

        val networkRequest = NetworkRequestMessage(
            origin = origin,
            path = path,
            query = queryParams.takeIf { it.isNotEmpty() },
            method = HttpMethod.fromString(request.method.lowercase()),
            headers = headers.takeIf { it.isNotEmpty() },
            formData = null, // TODO: Parse form data if needed
            body = request.body.toString(),
            timeout = null, // TODO: Extract from request if available
        )

        val requestLog = NetworkLog.Request(
            message = networkRequest,
            canceled = false,
            failed = false
        )

        val logMessage = LogMessage(
            location = location,
            message = LogMessageContent.NetworkRequest(requestLog),
            details = "HTTP Request",
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

        // Parse body and create modified response with cloned body
        val (body, modifiedResponse) = response.body?.let { responseBody ->
            val contentType = responseBody.contentType()?.toString() ?: ""

            try {
                if (contentType.contains("application/json") || contentType.startsWith("text/")) {
                    val bodyString = responseBody.string()
                    val clonedResponseBody = bodyString.toResponseBody(responseBody.contentType())
                    val newResponse = response.newBuilder().body(clonedResponseBody).build()
                    bodyString to newResponse
                } else {
                    val bytes = responseBody.bytes()
                    val clonedResponseBody = bytes.toResponseBody(responseBody.contentType())
                    val newResponse = response.newBuilder().body(clonedResponseBody).build()
                    Base64.getEncoder().encodeToString(bytes) to newResponse
                }
            } catch (e: IOException) {
                "[Error reading response body: ${e.message}]" to response
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
            location = location,
            message = LogMessageContent.NetworkResponse(responseLog),
            details = "HTTP Response",
        )

        logger.debug(logMessage)
        // to keep PNLogVerbosity.BODY functional
        if (logVerbosity == PNLogVerbosity.BODY) {
            val jsonMessage = mapperManager.toJson(logMessage)
            println("[$PUBNUB_OKHTTP_LOG_TAG] RESPONSE: $jsonMessage")
        }

        return modifiedResponse
    }

    private fun logError(request: Request, location: String, error: Exception, requestStartTime: Long) {
        val duration = System.currentTimeMillis() - requestStartTime

        val errorDetails = LogMessageContent.Error(
            message = ErrorDetails(
                type = error.javaClass.simpleName,
                message = error.message ?: "Unknown error",
                stack = error.stackTrace.take(10).map { it.toString() }
            )
        )

        val logMessage = LogMessage(
            location = location,
            message = errorDetails,
            details = "HTTP Request failed after ${duration}ms: ${error.message}",
        )

        logger.error(logMessage)
        // to keep PNLogVerbosity.BODY functional
        if (logVerbosity == PNLogVerbosity.BODY) {
            val jsonMessage = mapperManager.toJson(logMessage)
            println("[$PUBNUB_OKHTTP_LOG_TAG] ERROR: $jsonMessage")
        }
    }
}
