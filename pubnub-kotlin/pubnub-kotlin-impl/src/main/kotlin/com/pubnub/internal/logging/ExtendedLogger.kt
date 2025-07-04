package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage

// todo rename to PNLogger?
interface ExtendedLogger {
    fun trace(message: LogMessage)

    fun debug(message: LogMessage)

    fun info(message: LogMessage)

    fun warn(message: LogMessage)

    fun error(message: LogMessage)
}
