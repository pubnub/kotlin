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
            // Negative cap (e.g. -1) means "unlimited" — peek the full body. Long.MAX_VALUE is
            // safe as a peek upper bound; peekBody buffers from the source up to whatever the
            // body actually contains.
            val peekLimit = if (maxLoggedBodyBytes < 0L) {
                Long.MAX_VALUE
            } else {
                maxLoggedBodyBytes
            }

            try {
                val peeked = response.peekBody(peekLimit)
                val totalBytes = responseBody.contentLength()
                val truncated = maxLoggedBodyBytes > 0L && (
                    totalBytes > maxLoggedBodyBytes ||
                        (totalBytes < 0 && peeked.contentLength() == maxLoggedBodyBytes)
                )

                val text = if (contentType.contains("application/json") || contentType.startsWith("text/")) {
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
                        "$text… [truncated at $maxLoggedBodyBytes bytes, $totalDesc — set loggedHttpResponseMaxBytes (bytes) to a higher value to see more, 0 to disable HTTP response body logging, or a negative value to remove the limit]"
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

    // Returns a metadata-only descriptor for the request body — never reads body bytes. The interceptor
    // is global and shared by sendFile / S3 multipart uploads, so reading bodies here would risk
    // memory blowups. Publish/signal content is captured at the endpoint layer instead.
    //
    // contentLength() can throw on custom RequestBody subclasses (e.g. multipart bodies that lazily
    // measure their parts), and logRequest runs before chain.proceed(), so a throw here would abort
    // the HTTP request purely because logging tried to describe it. Catch Throwable to also contain
    // unchecked errors from custom subclasses.
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
