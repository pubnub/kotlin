package com.pubnub.api.models.consumer

import com.pubnub.api.PubNubException

fun MessageType(value: String): MessageType {
    return MessageType.UserDefined(value)
}

sealed interface MessageType {
    companion object {
        internal fun of(value: Int?): PNMessageType = when (value) {
            null, 0 -> Message
            1 -> Signal
            2 -> Object
            3 -> MessageAction
            4 -> File
            else -> throw PubNubException("Unknown message type value $value")
        }
    }

    data class UserDefined internal constructor(override val value: String) : MessageType, HistoryMessageType

    object Message : PNMessageType, HistoryMessageType {
        override val value: String = "pn_message"
    }

    object Signal : PNMessageType {
        override val value: String = "pn_signal"
    }

    object File : PNMessageType, HistoryMessageType {
        override val value: String = "pn_file"
    }

    object Object : PNMessageType {
        override val value: String = "pn_object"
    }

    object MessageAction : PNMessageType {
        override val value: String = "pn_messageAction"
    }

    val value: String
}

sealed interface HistoryMessageType : MessageType {
    companion object {
        internal fun of(value: Int?): HistoryMessageType = when (value) {
            null, 0 -> MessageType.Message
            4 -> MessageType.File
            else -> throw PubNubException("Unknown message type value $value")
        }
    }
}

sealed interface PNMessageType : MessageType
