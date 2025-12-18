package com.pubnub.api.enums

import platform.darwin.UInt32

/**
 * Swift SDK log level configuration.
 *
 * Uses bitmask flags that can be combined to create custom logging configurations.
 * Unlike hierarchical logging, each level must be explicitly enabled.
 *
 * Example:
 * ```kotlin
 * // Predefined combinations
 * LogLevel.Standard  // info, event, warn, error
 *
 * // Custom combination
 * LogLevel.custom(Level.ERROR, Level.WARN, Level.EVENT)
 * ```
 *
 * @see [Swift SDK Logging Documentation](https://www.pubnub.com/docs/sdks/swift/logging#log-levels)
 */
actual data class LogLevel(val levels: Set<Level>) {
    /**
     * Individual log level flags that can be combined.
     * Each level corresponds to a specific bitmask in the Swift SDK.
     */
    enum class Level(val value: UInt32) {
        /**
         * Logging is disabled (bitmask: 0).
         * Default setting.
         */
        NONE(0u),

        /**
         * Internal operations: method calls, state-machine transitions, detailed execution flow.
         * Bitmask: 1 << 0
         *
         * Warning: Logs sensitive information. Use only in development.
         */
        TRACE(1u shl 0),

        /**
         * User inputs, API parameters, HTTP requests and responses, operation results.
         * Bitmask: 1 << 1
         *
         * Warning: May log sensitive information. Use only in development.
         */
        DEBUG(1u shl 1),

        /**
         * Significant events including successful initialization and configuration changes.
         * Bitmask: 1 << 2
         */
        INFO(1u shl 2),

        /**
         * Internal PubNub operations or events.
         * Bitmask: 1 << 3
         */
        EVENT(1u shl 3),

        /**
         * Unusual conditions and non-breaking validation warnings.
         * Bitmask: 1 << 4
         */
        WARN(1u shl 4),

        /**
         * Errors, exceptions, and configuration conflicts.
         * Bitmask: 1 << 5
         */
        ERROR(1u shl 5),

        /**
         * All log levels will be captured.
         * Bitmask: UInt32.max
         *
         * Warning: Logs sensitive information. Never use in production.
         */
        ALL(UInt32.MAX_VALUE)
    }

    actual companion object {
        /** Logging disabled */
        actual val NONE = LogLevel(setOf(Level.NONE))

        /** Only errors */
        actual val ERROR = LogLevel(setOf(Level.ERROR))

        /** Warnings and errors */
        actual val WARN = LogLevel(setOf(Level.WARN, Level.ERROR))

        /** Info, event, warnings, and errors */
        actual val INFO = LogLevel(setOf(Level.INFO, Level.EVENT, Level.WARN, Level.ERROR))

        /**
         * Debug and above (excludes TRACE).
         * Warning: May log sensitive information.
         */
        actual val DEBUG = LogLevel(setOf(Level.DEBUG, Level.INFO, Level.EVENT, Level.WARN, Level.ERROR))

        /**
         * Everything including trace.
         * Warning: Logs sensitive information. Never use in production.
         */
        actual val TRACE = LogLevel(setOf(Level.TRACE, Level.DEBUG, Level.INFO, Level.EVENT, Level.WARN, Level.ERROR))

        /**
         * Production logging: info, event, warn, error.
         * Recommended for production environments.
         */
        val Standard = INFO

        /** All log levels */
        val All = LogLevel(setOf(Level.ALL))

        /** Default log level (logging disabled) */
        val DEFAULT = NONE

        /**
         * Create custom combination of log levels.
         *
         * Example:
         * ```kotlin
         * val customLogs = LogLevel.custom(
         *     Level.ERROR,
         *     Level.WARN,
         *     Level.EVENT
         * )
         * ```
         */
        fun custom(vararg levels: Level) = LogLevel(levels.toSet())
    }
}
