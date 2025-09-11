package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage

interface PNLogger {
    fun trace(message: LogMessage)

    fun debug(message: LogMessage)

    fun info(message: LogMessage)

    fun warn(message: LogMessage)

    fun error(message: LogMessage)
}
