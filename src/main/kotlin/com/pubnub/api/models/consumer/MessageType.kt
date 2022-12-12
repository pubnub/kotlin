package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException

sealed interface MessageType {
    companion object {
        operator fun invoke(value: String): MessageType {
            return UserDefined(value)
        }
        internal fun of(value: Int?): MessageType = when (value) {
            null, 0 -> Message()
            1 -> Signal()
            2 -> Object()
            3 -> MessageAction()
            4 -> File()
            else -> throw PubNubException("Unknown message type value $value")
        }
    }

    val type: String
}

data class UserDefined internal constructor(override val type: String) : MessageType

sealed interface PubNubMessageType : MessageType
data class Message internal constructor(override val type: String = "message") : PubNubMessageType
data class Signal internal constructor(override val type: String = "signal") : PubNubMessageType
data class File internal constructor(override val type: String = "file") : PubNubMessageType
data class Object internal constructor(override val type: String = "object") : PubNubMessageType
data class MessageAction internal constructor(override val type: String = "messageAction") : PubNubMessageType
