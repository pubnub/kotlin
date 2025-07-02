package com.pubnub.internal.logging

import com.pubnub.api.logging.LogMessage

interface ExtendedLogger  {
//    fun trace(type: LogMessageType, msg: String?) // todo do we need this
    fun trace(message: LogMessage)

//    fun debug(type: LogMessageType, msg: String?)
    fun debug(message: LogMessage)

//    fun info(type: LogMessageType, msg: String?) // todo zmien na fun info(msg: String?)
    fun info(message: LogMessage) // todo może tu dodać {}, jako default impl?

//    fun warn(type: LogMessageType, msg: String?)
    fun warn(message: LogMessage)

//    fun error(type: LogMessageType, msg: String?)
    fun error(message: LogMessage)

    // org.slf4j.Logger in configuration allows to configure timestamp, thread, level, logger name, etc.
    // so here we just take necessary elements
    fun getSimplifiedMessage(message: LogMessage): String {
        val simplifiedMessage =
            "pnInstanceId: ${message.pubNubId} location: ${message.location} details: ${message.details} message: ${message.message}"
        return simplifiedMessage
    }
}
