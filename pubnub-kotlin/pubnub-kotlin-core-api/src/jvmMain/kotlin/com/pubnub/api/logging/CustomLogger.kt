package com.pubnub.api.logging

/**
 * Interface for custom logger implementations.
 * Provides both string-based and structured LogMessage-based logging.
 *
 * Implementations receive every record the SDK produces. Per-record filtering (by level, format,
 * or sampling) is the implementation's responsibility — typically inside the [debug] / [trace] /
 * [info] / [warn] / [error] methods or in the downstream sink they delegate to. The SDK does not
 * route records based on a per-logger level threshold.
 *
 * The only knob the SDK consults on this interface before preparing expensive debug payloads is
 * [isDebugEnabled], and that is a **performance hint** — see its KDoc for details.
 */
interface CustomLogger {
    /**
     * The name of this logger implementation.
     */
    val name: String get() = "CustomLogger"

    /**
     * Log a trace message with a string.
     */
    fun trace(message: String?) {
        // Default implementation does nothing
    }

    /**
     * Log a trace message with structured data.
     */
    fun trace(logMessage: LogMessage) {
        // Default implementation does nothing
    }

    /**
     * Log a debug message with a string.
     */
    fun debug(message: String?) {
        // Default implementation does nothing
    }

    /**
     * Log a debug message with structured data.
     */
    fun debug(logMessage: LogMessage) {
        // Default implementation does nothing
    }

    /**
     * Log an info message with a string.
     */
    fun info(message: String?) {
        // Default implementation does nothing
    }

    /**
     * Log an info message with structured data.
     */
    fun info(logMessage: LogMessage) {
        // Default implementation does nothing
    }

    /**
     * Log a warning message with a string.
     */
    fun warn(message: String?) {
        // Default implementation does nothing
    }

    /**
     * Log a warning message with structured data.
     */
    fun warn(logMessage: LogMessage) {
        // Default implementation does nothing
    }

    /**
     * Log an error message with a string.
     */
    fun error(message: String?) {
        // Default implementation does nothing
    }

    /**
     * Log an error message with structured data.
     */
    fun error(logMessage: LogMessage) {
        // Default implementation does nothing
    }

    /**
     * Hint to the SDK whether preparing expensive DEBUG payloads (message fingerprinting,
     * UTF-8 truncation on publish/signal/subscribe) is worthwhile for this logger. Return
     * `false` if the downstream sink is configured at INFO or higher; the SDK will skip
     * that work unless another attached sink still needs DEBUG payloads.
     *
     * Not a delivery filter: [debug] is still invoked regardless. Per-record filtering is
     * the implementation's responsibility. Overriding is a pure performance optimization.
     */
    fun isDebugEnabled(): Boolean = true
}
