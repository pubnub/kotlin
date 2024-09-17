package com.pubnub.internal.models.server.pubsub

import com.pubnub.api.PubNubException

internal enum class MessageType {
    Message,
    Signal,
    Object,
    MessageAction,
    File;

    companion object {
        internal fun of(value: Int?): MessageType = when (value) {
            null, 0 -> Message
            1 -> Signal
            2 -> Object
            3 -> MessageAction
            4 -> File
            else -> throw PubNubException("Unknown message type value $value")
        }
    }
}
