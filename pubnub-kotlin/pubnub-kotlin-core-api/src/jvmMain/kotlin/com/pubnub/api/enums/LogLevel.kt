package com.pubnub.api.enums

/**
 * JVM LogLevel (ignored - use slf4j configuration instead).
 *
 * This parameter exists for API consistency across platforms but has no effect on JVM.
 * To configure logging on JVM, add an slf4j implementation (e.g., logback, log4j2)
 * and configure it according to that implementation's documentation.
 */
actual class LogLevel private constructor(private val name: String) {
    actual companion object {
        /** Logging disabled (JVM: configure via slf4j) */
        actual val NONE: LogLevel = LogLevel("NONE")

        actual val ERROR: LogLevel = LogLevel("ERROR")
        actual val WARN: LogLevel = LogLevel("WARN")
        actual val INFO: LogLevel = LogLevel("INFO")
        actual val DEBUG: LogLevel = LogLevel("DEBUG")
        actual val TRACE: LogLevel = LogLevel("TRACE")
    }

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is LogLevel) {
            return false
        }
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}
