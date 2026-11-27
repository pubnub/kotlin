package com.pubnub.docs.support

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import java.io.IOException

/**
 * [retryTransient] retries a call outside the snippet markers when the failure is transient (HTTP 5xx or 429, timeout,
 * network). It never retries a 4xx and never an AssertionError. A retried write can be stored twice, so tests that
 * count stored messages must not rely on it.
 *
 * `PubNubException` carries `statusCode` (0 when there was no HTTP answer) and a `PubNubError` for timeouts and
 * connection failures. PNStatusCategory is not used here: a failed call arrives as an exception, not as a status.
 */
object Retry {
    private val transientErrors = setOf(PubNubError.TIMEOUT, PubNubError.CONNECT_EXCEPTION)

    /** The HTTP status code of an exception or one of its causes, or null. */
    @JvmStatic
    fun statusCode(error: Throwable): Int? =
        generateSequence(error) { it.cause }
            .filterIsInstance<PubNubException>()
            .map { it.statusCode }
            .firstOrNull { it > 0 }

    @JvmStatic
    fun isTransient(error: Throwable): Boolean {
        if (error is AssertionError) {
            return false
        }
        val code = statusCode(error)
        if (code != null) {
            return code >= 500 || code == 429
        }
        return generateSequence(error) { it.cause }.any { cause ->
            (cause is PubNubException && cause.pubnubError in transientErrors) || cause is IOException
        }
    }

    fun <T> retryTransient(
        attempts: Int = 3,
        pauseMs: Long = 1000,
        label: String = "call",
        fn: () -> T,
    ): T {
        for (attempt in 1..attempts) {
            try {
                return fn()
            } catch (error: Exception) {
                if (attempt == attempts || !isTransient(error)) {
                    throw error
                }
                println("retryTransient: $label attempt $attempt failed (${error.javaClass.simpleName} ${statusCode(error)})")
                Thread.sleep(pauseMs * attempt)
            }
        }
        error("unreachable")
    }
}
