package com.pubnub.api.models.consumer.history

import com.pubnub.api.PubNubException

enum class HistoryMessageType(val value: Int) {
    Message(0),
    File(4),
    ;

    companion object {
        fun of(value: Int?): HistoryMessageType =
            when (value) {
                null, 0 -> Message
                4 -> File
                else -> throw PubNubException("Unknown message type value $value")
            }
    }
}
