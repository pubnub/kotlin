package com.pubnub.api.coroutine.model

import com.pubnub.api.models.consumer.message_actions.PNMessageAction

data class MessageActionEvent(
    val type: String,
    val value: String,
    val messageTimetoken: Long,
    val actionTimetoken: Long?,
    val publisher: String?
)

internal fun PNMessageAction.toMessageActionEvent(): MessageActionEvent {
    return MessageActionEvent(
        type = type,
        value = value,
        messageTimetoken = messageTimetoken,
        actionTimetoken = actionTimetoken,
        publisher = uuid
    )
}
