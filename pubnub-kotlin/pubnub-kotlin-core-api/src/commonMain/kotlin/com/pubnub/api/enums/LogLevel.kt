package com.pubnub.api.enums

/**
 * Platform-specific log level configuration.
 *
 * - **JVM**: This parameter is ignored. Configure logging via slf4j implementation.
 * - **JS**: Simple hierarchical log levels (NONE, ERROR, WARN, INFO, DEBUG, TRACE).
 * - **iOS**: Bitmask-based log levels that can be combined.
 */
expect class LogLevel {
    companion object {
        /** Logging disabled (default) */
        val NONE: LogLevel

        /** Logs only error messages */
        val ERROR: LogLevel

        /** Logs warnings and errors */
        val WARN: LogLevel

        /** Logs informational messages, warnings, and errors */
        val INFO: LogLevel

        /** Logs debug information and all higher severity levels */
        val DEBUG: LogLevel

        /** Most verbose logging level */
        val TRACE: LogLevel
    }
}
