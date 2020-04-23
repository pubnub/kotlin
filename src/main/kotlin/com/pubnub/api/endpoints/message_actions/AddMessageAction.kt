package com.pubnub.api.endpoints.message_actions

import com.google.gson.JsonObject
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class AddMessageAction(pubnub: PubNub) : Endpoint<EntityEnvelope<PNMessageAction>, PNAddMessageActionResult>(pubnub) {

    lateinit var channel: String
    lateinit var messageAction: PNMessageAction

    override fun validateParams() {
        super.validateParams()
        if (!::channel.isInitialized || channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (!::messageAction.isInitialized) {
            throw PubNubException(PubNubError.MESSAGE_ACTION_MISSING)
        }
        if (messageAction.type.isBlank()) {
            throw PubNubException(PubNubError.MESSAGE_ACTION_TYPE_MISSING)
        }
        if (messageAction.value.isBlank()) {
            throw PubNubException(PubNubError.MESSAGE_ACTION_VALUE_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNMessageAction>> {
        queryParams.putAll(encodeParams(queryParams))

        val body = JsonObject()
        body.addProperty("type", messageAction.type)
        body.addProperty("value", messageAction.value)

        return pubnub.retrofitManager.messageActionService
            .addMessageAction(
                subKey = pubnub.configuration.subscribeKey,
                channel = channel,
                messageTimetoken = messageAction.messageTimetoken.toString().toLowerCase(),
                body = body,
                options = queryParams
            )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNMessageAction>>): PNAddMessageActionResult? {
        return PNAddMessageActionResult(input.body()!!.data!!)
    }

    override fun operationType() = PNOperationType.PNAddMessageAction
}