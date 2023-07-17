package com.pubnub.api.endpoints

import com.google.gson.JsonElement
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

/**
 * @see [PubNub.messageCounts]
 */
class MessageCounts internal constructor(
    pubnub: PubNub,
    val channels: List<String>,
    val channelsTimetoken: List<Long>
) : Endpoint<JsonElement, PNMessageCountResult>(pubnub) {

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) throw PubNubException(PubNubError.CHANNEL_MISSING)
        if (channelsTimetoken.isEmpty()) throw PubNubException(PubNubError.TIMETOKEN_MISSING)
        if (channelsTimetoken.size != channels.size && channelsTimetoken.size > 1)
            throw PubNubException(PubNubError.CHANNELS_TIMETOKEN_MISMATCH)
    }

    override fun getAffectedChannels() = channels

    override fun doWork(queryParams: HashMap<String, String>): Call<JsonElement> {
        addQueryParams(queryParams)

        return pubnub.retrofitManager.historyService.fetchCount(
            subKey = pubnub.configuration.subscribeKey,
            channels = channels.toCsv(),
            options = queryParams
        )
    }

    override fun createResponse(input: Response<JsonElement>): PNMessageCountResult {
        val channelsMap = HashMap<String, Long>()

        val it = pubnub.mapper.getObjectIterator(input.body()!!, "channels")
        while (it.hasNext()) {
            val entry = it.next()
            channelsMap[entry.key] = entry.value.asLong
        }
        return PNMessageCountResult(channelsMap)
    }

    override fun operationType() = PNOperationType.PNMessageCountOperation

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (channelsTimetoken.size == 1) queryParams["timetoken"] = channelsTimetoken.toCsv()
        else queryParams["channelsTimetoken"] = channelsTimetoken.toCsv()
    }
}
