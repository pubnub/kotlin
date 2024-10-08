package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.removeMessageAction]
 */
class RemoveMessageActionEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channel: String,
    override val messageTimetoken: Long,
    override val actionTimetoken: Long,
) : EndpointCore<Void, PNRemoveMessageActionResult>(pubnub), RemoveMessageAction {
    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        return retrofitManager.messageActionService
            .deleteMessageAction(
                subKey = configuration.subscribeKey,
                channel = channel,
                messageTimetoken = messageTimetoken.toString().lowercase(Locale.getDefault()),
                actionTimetoken = actionTimetoken.toString().lowercase(Locale.getDefault()),
                options = queryParams,
            )
    }

    override fun createResponse(input: Response<Void>): PNRemoveMessageActionResult {
        return PNRemoveMessageActionResult()
    }

    override fun operationType() = PNOperationType.PNDeleteMessageAction

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_REACTION
}
