package com.pubnub.api.coroutine.model

import com.google.gson.JsonElement
import com.pubnub.api.models.consumer.pubsub.PNMessageResult

data class MessageEvent(
    val channel: String,
    val subscription: String?,
    val timetoken: Long?,
    val userMetadata: JsonElement?,
    val publisher: String?,
    val message: JsonElement,
    val messageActions: Collection<MessageActionEvent>
)

internal fun PNMessageResult.toMessageEvent(): MessageEvent {
    return MessageEvent(
        channel = channel,
        subscription = subscription,
        timetoken = timetoken,
        userMetadata = userMetadata,
        publisher = publisher,
        message = message,
        messageActions = listOf()
    )
}
