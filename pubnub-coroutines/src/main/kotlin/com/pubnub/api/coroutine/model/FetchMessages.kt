package com.pubnub.api.coroutine.model

import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.Action
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult

data class FetchMessages(
    val messages: Collection<MessageEvent>,
    val page: PNBoundedPage?
)

internal fun PNFetchMessagesResult.toFetchMessages(): FetchMessages {
    return FetchMessages(
        messages = channels.entries.flatMap { (channel, fetchMessageItems) ->
            fetchMessageItems.map { item ->
                MessageEvent(
                    channel = channel,
                    subscription = null,
                    timetoken = item.timetoken,
                    userMetadata = item.meta,
                    publisher = item.uuid,
                    message = item.message,
                    messageActions = item.actions.toMessageActionEvents(item.timetoken),
                )
            }
        },
        page = page
    )
}

internal fun Map<String, Map<String, List<Action>>>?.toMessageActionEvents(messageTimetoken: Long): List<MessageActionEvent> {
    return this?.entries?.flatMap { (type, valueAndActions) ->
        valueAndActions.entries.flatMap { (value, actions) ->
            actions.map {
                MessageActionEvent(
                    type = type,
                    value = value,
                    actionTimetoken = it.actionTimetoken.toLong(),
                    uuid = it.uuid,
                    messageTimetoken = messageTimetoken
                )
            }
        }
    } ?: listOf()
}
