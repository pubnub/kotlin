package com.pubnub.internal.endpoints

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.messageCounts]
 */
class MessageCountsEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channels: List<String>,
    override val channelsTimetoken: List<Long>,
) : EndpointCore<JsonElement, PNMessageCountResult>(pubnub), MessageCounts {
    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (channelsTimetoken.isEmpty()) {
            throw PubNubException(PubNubError.TIMETOKEN_MISSING)
        }
        if (channelsTimetoken.size != channels.size && channelsTimetoken.size > 1) {
            throw PubNubException(PubNubError.CHANNELS_TIMETOKEN_MISMATCH)
        }
    }

    override fun getAffectedChannels() = channels

    override fun doWork(queryParams: HashMap<String, String>): Call<JsonElement> {
        addQueryParams(queryParams)

        return retrofitManager.historyService.fetchCount(
            subKey = configuration.subscribeKey,
            channels = channels.toCsv(),
            options = queryParams,
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

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (channelsTimetoken.size == 1) {
            queryParams["timetoken"] = channelsTimetoken.toCsv()
        } else {
            queryParams["channelsTimetoken"] = channelsTimetoken.toCsv()
        }
    }
}
