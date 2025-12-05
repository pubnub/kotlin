package com.pubnub.api.enums

/**
 * JavaScript SDK log level.
 */
enum class LogLevel {
    /**
     * Logging is disabled.
     * Default setting.
     */
    NONE,

    /**
     * Logs only error messages.
     * Most restrictive level.
     */
    ERROR,

    /**
     * Logs warnings and errors.
     * Includes: WARN, ERROR
     */
    WARN,

    /**
     * Logs informational messages, warnings, and errors.
     * Includes: INFO, WARN, ERROR
     */
    INFO,

    /**
     * Logs debug information and all higher severity levels.
     * Includes: DEBUG, INFO, WARN, ERROR
     *
     * Warning: May log sensitive information including API keys and message content.
     * Use only in development environments.
     */
    DEBUG,

    /**
     * Most verbose logging level. Logs all messages including internal traces.
     * Includes: TRACE, DEBUG, INFO, WARN, ERROR
     *
     * Warning: Logs sensitive information. Never enable in production.
     */
    TRACE;

    companion object {
        /** Default log level (logging disabled) */
        val DEFAULT = NONE
    }
}
