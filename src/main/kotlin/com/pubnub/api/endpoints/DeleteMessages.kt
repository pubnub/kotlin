package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap
import java.util.Locale

/**
 * @see [PubNub.deleteMessages]
 */
class DeleteMessages internal constructor(
    pubnub: PubNub,
    val channels: List<String>,
    val start: Long? = null,
    val end: Long? = null
) : Endpoint<Void, PNDeleteMessagesResult>(pubnub) {

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        addQueryParams(queryParams)

        return pubnub.retrofitManager.historyService.deleteMessages(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams
        )
    }

    override fun createResponse(input: Response<Void>): PNDeleteMessagesResult =
        PNDeleteMessagesResult()

    override fun operationType() = PNOperationType.PNDeleteMessagesOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        start?.run { queryParams["start"] = this.toString().lowercase(Locale.US) }
        end?.run { queryParams["end"] = this.toString().lowercase(Locale.US) }
    }
}
