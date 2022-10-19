package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap
import java.util.Locale

interface DeleteMessages : RemoteAction<PNDeleteMessagesResult>

/**
 * @see [PubNub.deleteMessages]
 */
class DeleteMessagesImpl(
    pubnub: PubNub,
    val channels: List<String>,
    val start: Long? = null,
    val end: Long? = null
) : Endpoint<Void, PNDeleteMessagesResult>(pubnub), DeleteMessages {

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

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        start?.run { queryParams["start"] = this.toString().toLowerCase(Locale.US) }
        end?.run { queryParams["end"] = this.toString().toLowerCase(Locale.US) }
    }
}
