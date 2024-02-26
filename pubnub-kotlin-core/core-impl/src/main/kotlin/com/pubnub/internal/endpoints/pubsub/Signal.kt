package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.InternalPubNubClient
import retrofit2.Call
import retrofit2.Response

/**
 * @see [InternalPubNubClient.signal]
 */
class Signal internal constructor(
    pubnub: InternalPubNubClient,
    override val channel: String,
    override val message: Any,
) : Endpoint<List<Any>, PNPublishResult>(pubnub), ISignal {
    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        return pubnub.retrofitManager.signalService.signal(
            pubKey = pubnub.configuration.publishKey,
            subKey = pubnub.configuration.subscribeKey,
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
