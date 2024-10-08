package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.getMessageActions]
 */
class GetMessageActionsEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channel: String,
    override val page: PNBoundedPage,
) : EndpointCore<PNGetMessageActionsResult, PNGetMessageActionsResult>(pubnub), GetMessageActions {
    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<PNGetMessageActionsResult> {
        addQueryParams(queryParams)

        return retrofitManager.messageActionService
            .getMessageActions(
                configuration.subscribeKey,
                channel,
                queryParams,
            )
    }

    override fun createResponse(input: Response<PNGetMessageActionsResult>): PNGetMessageActionsResult =
        PNGetMessageActionsResult(
            actions = input.body()!!.actions,
            page = input.body()!!.page,
        )

    override fun operationType() = PNOperationType.PNGetMessageActions

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_REACTION

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        page.start?.run { queryParams["start"] = this.toString().lowercase(Locale.getDefault()) }
        page.end?.run { queryParams["end"] = this.toString().lowercase(Locale.getDefault()) }
        page.limit?.run { queryParams["limit"] = this.toString().lowercase(Locale.getDefault()) }
    }
}
