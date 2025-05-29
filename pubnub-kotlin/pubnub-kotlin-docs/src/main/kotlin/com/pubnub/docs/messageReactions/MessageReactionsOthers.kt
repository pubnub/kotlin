package com.pubnub.docs.messageReactions

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.v2.callbacks.Result

class MessageReactionsOthers {
    private fun removeMessageActionBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/message-actions#basic-usage-1

        // snippet.removeMessageActionBasic
        pubnub.removeMessageAction(
            channel = "my_channel",
            messageTimetoken = 15701761818730000L,
            actionTimetoken = 15701775691010000L
        ).async { result: Result<PNRemoveMessageActionResult> ->
            result.onSuccess { res: PNRemoveMessageActionResult ->
                // result has no actionable data
                // it's enough to check if the status itself is not an error
            }.onFailure { e: PubNubException ->
                // do something with the exception
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end
    }

    private fun getMessageActionsBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/message-actions#basic-usage-2

        // snippet.getMessageActionsBasic
        pubnub.getMessageActions(
            channel = "my_channel"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun getMessageActionsWithPaging(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/message-actions#other-examples

        // snippet.getMessageActionsWithPaging
        fun getMessageActionsWithPaging(
            channel: String,
            start: Long,
            callback: (actions: List<PNMessageAction>) -> Unit
        ) {
            pubnub.getMessageActions(
                channel = channel,
                page = PNBoundedPage(limit = 5, start = start)
            ).async { result: Result<PNGetMessageActionsResult> ->
                result.onSuccess { getMessageActionsResult: PNGetMessageActionsResult ->
                    if (getMessageActionsResult.actions.isNotEmpty()) {
                        callback.invoke(getMessageActionsResult.actions)
                        getMessageActionsWithPaging(
                            channel,
                            getMessageActionsResult.actions.first().actionTimetoken!!,
                            callback
                        )
                    } else {
                        callback.invoke(emptyList())
                    }
                }.onFailure { exception: PubNubException ->
                    // Handle error
                }
            }
        }

        // Usage example
        getMessageActionsWithPaging(
            channel = "my_channel",
            start = System.currentTimeMillis() * 10_000L
        ) { actions ->
            actions.forEach {
                println(it.type)
                println(it.value)
                println(it.uuid)
                println(it.messageTimetoken)
                println(it.actionTimetoken)
            }
        }
        // snippet.end
    }
}
