package com.pubnub.docs.messageReactions
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/message-actions#other-examples

// snippet.getMessageActionsWithPaging
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.v2.PNConfiguration

fun main() {
    val userId = UserId("message-action-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging for visibility
    }.build()

    val pubnub = PubNub.create(config)
    val channelName = "my_channel"
    val channel = pubnub.channel(channelName)

    val pnPublishResult = channel.publish(message = "Hello, world!").sync()

    pubnub.addMessageAction(
        channel = channelName,
        messageAction =
            PNMessageAction(
                type = "someother",
                value = "smiley",
                messageTimetoken = pnPublishResult.timetoken,
            ),
    ).sync()

    getMessageActionsWithPaging(
        pubNub = pubnub,
        channel = channelName,
        start = null // Start with no start value to get the most recent actions
    ) { actions ->
        println("Fetched ${actions.size} message actions:")
        actions.forEach {
            println("Action Type: ${it.type}")
            println("Action Value: ${it.value}")
            println("UUID: ${it.uuid}")
            println("Message Timetoken: ${it.messageTimetoken}")
            println("Action Timetoken: ${it.actionTimetoken}")
        }
    }
}

/**
 * Fetches 5 message reactions at a time, recursively and in a paged manner.
 *
 * @param channel  The channel where the message is published, to fetch message reactions from.
 * @param start    The timetoken which indicates from where to start fetching message reactions.
 * @param callback The callback to dispatch fetched message reactions to.
 */
fun getMessageActionsWithPaging(
    pubNub: PubNub,
    channel: String,
    start: Long?,
    callback: (actions: List<PNMessageAction>) -> Unit
) {
    val page = start?.let { PNBoundedPage(limit = 5, start = it) } ?: PNBoundedPage(limit = 5)
    pubNub.getMessageActions(
        channel = channel,
        page = page
    ).async { result ->
        result.onSuccess { getMessageActionsResult: PNGetMessageActionsResult ->
            if (getMessageActionsResult.actions.isNotEmpty()) {
                callback.invoke(getMessageActionsResult.actions)
                getMessageActionsWithPaging(
                    pubNub,
                    channel,
                    getMessageActionsResult.actions.first().actionTimetoken,
                    callback
                )
            } else {
                callback.invoke(emptyList())
            }
        }.onFailure { exception: PubNubException ->
            println("Error fetching message actions: $exception")
        }
    }
}
// snippet.end
