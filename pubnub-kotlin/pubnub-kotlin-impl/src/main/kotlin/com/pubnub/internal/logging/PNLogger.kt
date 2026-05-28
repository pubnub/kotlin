package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage

interface PNLogger {
    fun trace(message: LogMessage)

    fun debug(message: LogMessage)

    fun info(message: LogMessage)

    fun warn(message: LogMessage)

    fun error(message: LogMessage)

    /**
     * Producer-side hint: should the caller pay to prepare an expensive DEBUG payload?
     *
     * Returns `true` when at least one attached sink wants DEBUG payloads prepared. This is a
     * hint, not a delivery filter — sinks still receive every record produced via [debug];
     * per-record filtering is each sink's own responsibility.
     */
    fun isDebugEnabled(): Boolean
}
