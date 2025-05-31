package com.pubnub.docs.messageReactions

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
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
}
