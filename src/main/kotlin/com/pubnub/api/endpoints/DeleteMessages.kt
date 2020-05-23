package com.pubnub.api.endpoints

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.models.server.DeleteMessagesEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class DeleteMessages(pubnub: PubNub) : Endpoint<DeleteMessagesEnvelope, PNDeleteMessagesResult>(pubnub) {

    lateinit var channels: List<String>
    var start: Long? = null
    var end: Long? = null

    override fun validateParams() {
        super.validateParams()
        if (!::channels.isInitialized || channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<DeleteMessagesEnvelope> {
        start?.let {
            queryParams["start"] = it.toString().toLowerCase(Locale.US)
        }
        end?.let {
            queryParams["end"] = it.toString().toLowerCase(Locale.US)
        }

        return pubnub.retrofitManager.historyService.deleteMessages(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams
        )
    }

    override fun createResponse(input: Response<DeleteMessagesEnvelope>): PNDeleteMessagesResult? {
        if (input.body()!!.status != 200) {
            throw PubNubException(PubNubError.PARSING_ERROR)
        }
        return PNDeleteMessagesResult()
    }

    override fun operationType() = PNOperationType.PNDeleteMessagesOperation
}