package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class GetMessageActions(pubnub: PubNub) :
    Endpoint<EntityEnvelope<List<PNMessageAction>>, PNGetMessageActionsResult>(pubnub) {

    lateinit var channel: String
    var start: Long? = null
    var end: Long? = null
    var limit: Int? = null

    override fun validateParams() {
        super.validateParams()
        if (!::channel.isInitialized || channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<List<PNMessageAction>>> {

        start?.let { queryParams["start"] = it.toString().toLowerCase() }
        end?.let { queryParams["end"] = it.toString().toLowerCase() }
        limit?.let { queryParams["limit"] = it.toString().toLowerCase() }

        return pubnub.retrofitManager.messageActionService
            .getMessageActions(
                pubnub.configuration.subscribeKey,
                channel,
                queryParams
            )
    }

    override fun createResponse(input: Response<EntityEnvelope<List<PNMessageAction>>>): PNGetMessageActionsResult? {
        return PNGetMessageActionsResult().apply {
            actions = input.body()!!.data!!
        }
    }

    override fun operationType() = PNOperationType.PNGetMessageActions
}