package com.pubnub.api.enums

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
    enum class Level {
        /**
         * Logging is disabled (bitmask: 0).
         * Default setting.
         */
        NONE,

        /**
         * Internal operations: method calls, state-machine transitions, detailed execution flow.
         * Bitmask: 1 << 0
         *
         * Warning: Logs sensitive information. Use only in development.
         */
        TRACE,

        /**
         * User inputs, API parameters, HTTP requests and responses, operation results.
         * Bitmask: 1 << 1
         *
         * Warning: May log sensitive information. Use only in development.
         */
        DEBUG,

        /**
         * Significant events including successful initialization and configuration changes.
         * Bitmask: 1 << 2
         */
        INFO,

        /**
         * Internal PubNub operations or events.
         * Bitmask: 1 << 3
         */
        EVENT,

        /**
         * Unusual conditions and non-breaking validation warnings.
         * Bitmask: 1 << 4
         */
        WARN,

        /**
         * Errors, exceptions, and configuration conflicts.
         * Bitmask: 1 << 5
         */
        ERROR,

        /**
         * All log levels will be captured.
         * Bitmask: UInt32.max
         *
         * Warning: Logs sensitive information. Never use in production.
         */
        ALL
    }

    actual companion object {

        /** Logging disabled */
        val None = LogLevel(setOf(Level.NONE))
        actual val NONE = None

        /** Only errors */
        val Error = LogLevel(setOf(Level.ERROR))

        /** Warnings and errors */
        val Warn = LogLevel(setOf(Level.WARN, Level.ERROR))

        /**
         * Production logging: info, event, warn, error.
         * Recommended for production environments.
         */
        val Standard = LogLevel(setOf(Level.INFO, Level.EVENT, Level.WARN, Level.ERROR))

        /** Info, event, warnings, and errors */
        val Info = LogLevel(setOf(Level.INFO, Level.EVENT, Level.WARN, Level.ERROR))

        /**
         * Debug and above (excludes TRACE).
         * Warning: May log sensitive information.
         */
        val Debug = LogLevel(setOf(Level.DEBUG, Level.INFO, Level.EVENT, Level.WARN, Level.ERROR))

        /**
         * Everything including trace.
         * Warning: Logs sensitive information. Never use in production.
         */
        val All = LogLevel(setOf(Level.ALL))

        /** Default log level (logging disabled) */
        val DEFAULT = None

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
