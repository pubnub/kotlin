package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.signal]
 */
class SignalEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channel: String,
    override val message: Any,
    override val customMessageType: String? = null
) : EndpointCore<List<Any>, PNPublishResult>(pubnub), Signal {
    companion object {
        private const val CUSTOM_MESSAGE_TYPE_QUERY_PARAM = "custom_message_type"
    }

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        customMessageType?.let { customMessageTypeNotNull ->
            queryParams[CUSTOM_MESSAGE_TYPE_QUERY_PARAM] = customMessageTypeNotNull
        }

        return retrofitManager.signalService.signal(
            pubKey = configuration.publishKey,
            subKey = configuration.subscribeKey,
            channel = channel,
            message = pubnub.mapper.toJson(message),
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult =
        PNPublishResult(
            timetoken = input.body()!![2].toString().toLong(),
        )

    override fun operationType() = PNOperationType.PNSignalOperation

    override fun isPubKeyRequired() = true

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUBLISH
}
