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
import java.util.HashMap

/**
 * @see [PubNub.getMessageActions]
 */
class GetMessageActions internal constructor(
    pubnub: PubNub,
    val channel: String,
    val start: Long? = null,
    val end: Long? = null,
    val limit: Int? = null
) : Endpoint<EntityEnvelope<List<PNMessageAction>>, PNGetMessageActionsResult>(pubnub) {

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<List<PNMessageAction>>> {

        addQueryParams(queryParams)

        return pubnub.retrofitManager.messageActionService
            .getMessageActions(
                pubnub.configuration.subscribeKey,
                channel,
                queryParams
            )
    }

    override fun createResponse(input: Response<EntityEnvelope<List<PNMessageAction>>>): PNGetMessageActionsResult =
        PNGetMessageActionsResult(
            actions = input.body()!!.data!!
        )

    override fun operationType() = PNOperationType.PNGetMessageActions

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        start?.run { queryParams["start"] = this.toString().toLowerCase() }
        end?.run { queryParams["end"] = this.toString().toLowerCase() }
        limit?.run { queryParams["limit"] = this.toString().toLowerCase() }
    }
}
