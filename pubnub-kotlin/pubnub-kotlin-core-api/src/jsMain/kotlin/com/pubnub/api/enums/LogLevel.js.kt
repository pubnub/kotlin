package com.pubnub.api.enums

/**
 * JavaScript SDK log level.
 */
actual class LogLevel private constructor(private val name: String, internal val level: Int) {
    actual companion object {
        /**
         * Logging is disabled.
         * Default setting.
         */
        actual val NONE: LogLevel = LogLevel("NONE", 0)

        /**
         * Logs only error messages.
         * Most restrictive level.
         */
        val ERROR: LogLevel = LogLevel("ERROR", 1)

        /**
         * Logs warnings and errors.
         * Includes: WARN, ERROR
         */
        val WARN: LogLevel = LogLevel("WARN", 2)

        /**
         * Logs informational messages, warnings, and errors.
         * Includes: INFO, WARN, ERROR
         */
        val INFO: LogLevel = LogLevel("INFO", 3)

        /**
         * Logs debug information and all higher severity levels.
         * Includes: DEBUG, INFO, WARN, ERROR
         *
         * Warning: May log sensitive information including API keys and message content.
         * Use only in development environments.
         */
        val DEBUG: LogLevel = LogLevel("DEBUG", 4)

        /**
         * Most verbose logging level. Logs all messages including internal traces.
         * Includes: TRACE, DEBUG, INFO, WARN, ERROR
         *
         * Warning: Logs sensitive information. Never enable in production.
         */
        val TRACE: LogLevel = LogLevel("TRACE", 5)

        val DEFAULT = NONE
    }

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogLevel) return false
        return level == other.level
    }

    override fun hashCode(): Int = level
}
