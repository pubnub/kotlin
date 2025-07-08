package com.pubnub.api.logging

/**
 * Interface for custom logger implementations.
 * Provides both string-based and structured LogMessage-based logging.
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
}
