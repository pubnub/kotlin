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

        // Additional common constants can be added
    }
}