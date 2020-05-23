package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class RemoveMessageAction(pubnub: PubNub) : Endpoint<EntityEnvelope<Any>, PNRemoveMessageActionResult>(pubnub) {

    lateinit var channel: String
    var messageTimetoken: Long? = null
    var actionTimetoken: Long? = null

    override fun validateParams() {
        super.validateParams()
        if (!::channel.isInitialized || channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (messageTimetoken == null) {
            throw PubNubException(PubNubError.MESSAGE_TIMETOKEN_MISSING)
        }
        if (actionTimetoken == null) {
            throw PubNubException(PubNubError.MESSAGE_ACTION_TIMETOKEN_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any>> {
        return pubnub.retrofitManager.messageActionService
            .deleteMessageAction(
                subKey = pubnub.configuration.subscribeKey,
                channel = channel,
                messageTimetoken = messageTimetoken.toString().toLowerCase(),
                actionTimetoken = actionTimetoken.toString().toLowerCase(),
                options = queryParams
            )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any>>): PNRemoveMessageActionResult? {
        input.body()!!.data!!
        return PNRemoveMessageActionResult()
    }

    override fun operationType() = PNOperationType.PNAddMessageAction
}