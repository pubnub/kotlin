package com.pubnub.api.logging

/**
 * Controls what (and how much of) the SDK includes inside individual debug log entries.
 *
 * Logging *delivery* — sinks, levels, formatting — is configured via an SLF4J implementation
 * (e.g. logback) or a [CustomLogger]. This type only governs the content the SDK renders into
 * each log record before it reaches the logger.
 *
 * @property loggedMessageContentMaxBytes Maximum size, in UTF-8 bytes, of message content
 *   rendered into the publish/signal/subscribe debug log entry. Set to `0` to replace the
 *   content field with a placeholder marker noting that content logging is disabled.
 *   Negative values are tolerated and behave the same as `0`.
 *
 *   Defaults to `500`.
 *
 * @property loggedHttpResponseMaxBytes Maximum size, in bytes, of the HTTP response body
 *   peeked into the network-response debug log entry. Set to `0` to replace the body field
 *   with a placeholder marker noting that body logging is disabled. Negative values are
 *   tolerated and behave the same as `0`.
 *
 *   Defaults to `2000`.
 */
data class LogContentConfig
    @JvmOverloads
    constructor(
        val loggedMessageContentMaxBytes: Int = DEFAULT_LOGGED_MESSAGE_CONTENT_MAX_BYTES,
        val loggedHttpResponseMaxBytes: Int = DEFAULT_LOGGED_HTTP_RESPONSE_MAX_BYTES,
    ) {
        companion object {
            const val DEFAULT_LOGGED_MESSAGE_CONTENT_MAX_BYTES = 500
            const val DEFAULT_LOGGED_HTTP_RESPONSE_MAX_BYTES = 2000
        }
    }
