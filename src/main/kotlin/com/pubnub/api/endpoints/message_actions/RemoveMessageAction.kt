package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.retry.RetryableEndpointGroup
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNub.removeMessageAction]
 */
class RemoveMessageAction internal constructor(
    pubnub: PubNub,
    val channel: String,
    val messageTimetoken: Long,
    val actionTimetoken: Long
) : Endpoint<Void, PNRemoveMessageActionResult>(pubnub) {

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        return pubnub.retrofitManager.messageActionService
            .deleteMessageAction(
                subKey = pubnub.configuration.subscribeKey,
                channel = channel,
                messageTimetoken = messageTimetoken.toString().lowercase(Locale.getDefault()),
                actionTimetoken = actionTimetoken.toString().lowercase(Locale.getDefault()),
                options = queryParams
            )
    }

    override fun createResponse(input: Response<Void>): PNRemoveMessageActionResult? {
        return PNRemoveMessageActionResult()
    }

    override fun operationType() = PNOperationType.PNDeleteMessageAction

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_REACTION
}
