package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

interface GetMessageActions : RemoteAction<PNGetMessageActionsResult>

/**
 * @see [PubNub.getMessageActions]
 */
class GetMessageActionsImpl(
    pubnub: PubNub,
    val channel: String,
    val page: PNBoundedPage
) : Endpoint<PNGetMessageActionsResult, PNGetMessageActionsResult>(pubnub), GetMessageActions {

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<PNGetMessageActionsResult> {

        addQueryParams(queryParams)

        return pubnub.retrofitManager.messageActionService
            .getMessageActions(
                pubnub.configuration.subscribeKey,
                channel,
                queryParams
            )
    }

    override fun createResponse(input: Response<PNGetMessageActionsResult>): PNGetMessageActionsResult =
        PNGetMessageActionsResult(
            actions = input.body()!!.actions,
            page = input.body()!!.page
        )

    override fun operationType() = PNOperationType.PNGetMessageActions

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        page.start?.run { queryParams["start"] = this.toString().toLowerCase() }
        page.end?.run { queryParams["end"] = this.toString().toLowerCase() }
        page.limit?.run { queryParams["limit"] = this.toString().toLowerCase() }
    }
}
