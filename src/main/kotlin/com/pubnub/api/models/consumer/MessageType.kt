package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException

fun MessageType(value: String): MessageType {
    return UserDefined(value)
}

sealed interface MessageType {
    companion object {
        internal fun of(value: Int?): MessageType = when (value) {
            null, 0 -> Message()
            1 -> Signal()
            2 -> Object()
            3 -> MessageAction()
            4 -> File()
            else -> throw PubNubException("Unknown message type value $value")
        }
    }

    val value: String
}

data class UserDefined internal constructor(override val value: String) : MessageType

sealed interface PNMessageType : MessageType
data class Message internal constructor(override val value: String = "message") : PNMessageType
data class Signal internal constructor(override val value: String = "signal") : PNMessageType
data class File internal constructor(override val value: String = "file") : PNMessageType
data class Object internal constructor(override val value: String = "object") : PNMessageType
data class MessageAction internal constructor(override val value: String = "messageAction") : PNMessageType
