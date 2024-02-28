package com.pubnub.internal.endpoints

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.CoreEndpoint
import com.pubnub.internal.CorePubNubClient
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [CorePubNubClient.deleteMessages]
 */
class DeleteMessagesEndpoint internal constructor(
    pubnub: CorePubNubClient,
    override val channels: List<String>,
    override val start: Long? = null,
    override val end: Long? = null,
) : CoreEndpoint<Void, PNDeleteMessagesResult>(pubnub), DeleteMessagesInterface {
    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        addQueryParams(queryParams)

        return pubnub.retrofitManager.historyService.deleteMessages(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams,
        )
    }

    override fun createResponse(input: Response<Void>): PNDeleteMessagesResult = PNDeleteMessagesResult()

    override fun operationType() = PNOperationType.PNDeleteMessagesOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        start?.run { queryParams["start"] = this.toString().lowercase(Locale.US) }
        end?.run { queryParams["end"] = this.toString().lowercase(Locale.US) }
    }
}
