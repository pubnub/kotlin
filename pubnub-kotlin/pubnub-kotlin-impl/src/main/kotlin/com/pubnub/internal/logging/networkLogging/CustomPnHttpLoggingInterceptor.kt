package com.pubnub.internal.logging.networkLogging

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.logging.HttpMethod
import com.pubnub.api.logging.LogContentConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.managers.MapperManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.util.Base64

private const val PUBNUB_OKHTTP_LOG_TAG = "pubnub.okhttp"

class CustomPnHttpLoggingInterceptor(
    private val logger: PNLogger,
    private val mapperManager: MapperManager,
    private val logVerbosity: PNLogVerbosity,
    private val maxLoggedBodyBytes: Long,
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
            body = describeRequestBody(request.body),
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
        // Capped at maxLoggedBodyBytes to avoid flooding logs with large payloads.
        // Catch Throwable so a bad cap or unchecked decode error in the logging path never
        // aborts the real HTTP request — consistent with describeRequestBody().
        val body: String? = response.body?.let { responseBody ->
            if (maxLoggedBodyBytes == 0L) {
                return@let "[...]"
            }
            val contentType = responseBody.contentType()?.toString() ?: ""

            try {
                val peeked = response.peekBody(maxLoggedBodyBytes)
                val totalBytes = responseBody.contentLength()
                val truncated = maxLoggedBodyBytes > 0L && (
                    totalBytes > maxLoggedBodyBytes ||
                        (totalBytes < 0 && peeked.contentLength() == maxLoggedBodyBytes)
                )

                // Log as readable text for JSON and text bodies. `contains("+json")` covers RFC 6839
                // structured-syntax-suffix vendor types such as PubNub's Objects/DataSync responses
                // (e.g. "application/vnd.pubnub.objects.entity+json"), which are plain JSON but do not
                // contain the literal "application/json". Everything else falls back to Base64 so
                // binary/unknown bodies can't corrupt the log envelope.
                val text = if (
                    contentType.contains("application/json") ||
                    contentType.contains("+json") ||
                    contentType.startsWith("text/")
                ) {
                    val charset = responseBody.contentType()?.charset() ?: Charsets.UTF_8
                    peeked.source().readString(charset)
                } else {
                    Base64.getEncoder().encodeToString(peeked.bytes())
                }

                if (truncated) {
                    if (maxLoggedBodyBytes == LogContentConfig.DEFAULT_LOGGED_HTTP_RESPONSE_MAX_BYTES.toLong()) {
                        val totalDesc = if (totalBytes >= 0) {
                            "$totalBytes bytes total"
                        } else {
                            "total size unknown"
                        }
                        "$text… [truncated at $maxLoggedBodyBytes bytes, $totalDesc — set loggedHttpResponseMaxBytes (bytes) to a higher value to see more, or 0 to disable HTTP response body logging]"
                    } else {
                        "$text…"
                    }
                } else {
                    text
                }
            } catch (t: Throwable) {
                "[Error reading response body: ${t.message}]"
            }
        }

        val logMessage = LogMessage(
            message = LogMessageContent.NetworkResponse(
                url = url,
                status = status,
                headers = headers.takeIf { it.isNotEmpty() },
                body = body,
                protocol = response.protocol.toString(),
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

    // Describes the request body for logging. For textual bodies (JSON / +json vendor types / text/*)
    // that fit within maxLoggedBodyBytes and are known not to be one-shot streams, the actual content
    // is buffered and logged so request payloads (e.g. DataSync create) are visible. Everything else
    // — binary bodies, oversized bodies, one-shot streams, unknown-length bodies — keeps a metadata-only
    // descriptor and never reads bytes.
    //
    // The interceptor is global and shared by sendFile / S3 multipart uploads, so unconditionally
    // reading bodies here would risk memory blowups; the content-type + size + one-shot guards keep
    // those on the metadata-only path.
    //
    // contentLength() can throw on custom RequestBody subclasses (e.g. multipart bodies that lazily
    // measure their parts), and logRequest runs before chain.proceed(), so a throw here would abort
    // the HTTP request purely because logging tried to describe it. Catch Throwable to also contain
    // unchecked errors from custom subclasses / body reads.
    private fun describeRequestBody(body: RequestBody?): String? {
        if (body == null) {
            return null
        }
        val contentType = body.contentType()?.toString() ?: "unknown"
        val length = try {
            body.contentLength()
        } catch (_: Throwable) {
            -1L
        }

        val isTextual = contentType.contains("application/json") ||
            contentType.contains("+json") ||
            contentType.startsWith("text/")
        val loggable = isTextual &&
            maxLoggedBodyBytes > 0L &&
            !body.isOneShot() &&
            length in 0..maxLoggedBodyBytes

        if (loggable) {
            try {
                val buffer = okio.Buffer()
                body.writeTo(buffer)
                val charset = body.contentType()?.charset() ?: Charsets.UTF_8
                return buffer.readString(charset)
            } catch (_: Throwable) {
                // Fall through to the metadata-only descriptor below.
            }
        }

        return if (length >= 0) {
            "[body not logged: $length bytes, contentType=$contentType]"
        } else {
            "[body not logged: unknown size, contentType=$contentType]"
        }
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
