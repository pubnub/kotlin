package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import retrofit2.Call
import retrofit2.Response

class Signal(pubnub: PubNub) : Endpoint<List<Any>, PNPublishResult>(pubnub) {

    lateinit var channel: String
    lateinit var message: Any

    private fun isChannelValid() = ::channel.isInitialized
    private fun isMessageValid() = ::message.isInitialized

    override fun validateParams() {
        super.validateParams()
        if (!isChannelValid() || channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (!isMessageValid()) {
            throw PubNubException(PubNubError.MESSAGE_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        return pubnub.retrofitManager.signalService.signal(
            pubKey = pubnub.configuration.publishKey,
            subKey = pubnub.configuration.subscribeKey,
            channel = channel,
            message = pubnub.mapper.toJson(message),
            options = queryParams
        )
    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult? {
        return PNPublishResult(
            timetoken = input.body()!![2].toString().toLong()
        )
    }

    override fun operationType() = PNOperationType.PNSignalOperation

    override fun isPubKeyRequired() = true

}