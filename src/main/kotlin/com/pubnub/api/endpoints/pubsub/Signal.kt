package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.api.models.consumer.PNPublishResult
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.signal]
 */
class Signal internal constructor(
    pubnub: PubNub,
    val channel: String,
    val message: Any,
    val spaceId: SpaceId? = null,
    val messageType: MessageType? = null
) : Endpoint<List<Any>, PNPublishResult>(pubnub) {

    companion object {
        internal const val SPACE_ID_QUERY_PARAM = "space-id"
        internal const val MESSAGE_TYPE_QUERY_PARAM = "type"
    }

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {

        spaceId?.run { queryParams[SPACE_ID_QUERY_PARAM] = spaceId.value }

        messageType?.run { queryParams[MESSAGE_TYPE_QUERY_PARAM] = messageType.value }

        return pubnub.retrofitManager.signalService.signal(
            pubKey = pubnub.configuration.publishKey,
            subKey = pubnub.configuration.subscribeKey,
            channel = channel,
            message = pubnub.mapper.toJson(message),
            options = queryParams
        )
    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult =
        PNPublishResult(
            timetoken = input.body()!![2].toString().toLong()
        )

    override fun operationType() = PNOperationType.PNSignalOperation

    override fun isPubKeyRequired() = true
}
