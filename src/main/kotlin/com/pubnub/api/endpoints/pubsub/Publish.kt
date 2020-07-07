package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.vendor.Crypto
import retrofit2.Call
import retrofit2.Response

class Publish(pubnub: PubNub) : Endpoint<List<Any>, PNPublishResult>(pubnub) {

    lateinit var channel: String
    lateinit var message: Any
    lateinit var meta: Any
    var shouldStore: Boolean? = null
    var usePost = false
    var replicate = true
    var ttl: Int? = null

    private fun isChannelValid() = ::channel.isInitialized
    private fun isMessageValid() = ::message.isInitialized
    private fun isMetaValid() = ::meta.isInitialized

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

        var stringifiedMessage = pubnub.mapper.toJson(message)

        if (isMetaValid()) {
            queryParams["meta"] = pubnub.mapper.toJson(meta)
        }

        shouldStore?.run { queryParams["store"] = if (this) "1" else "0" }

        ttl?.let { queryParams["ttl"] = it.toString() }

        if (!replicate) queryParams["norep"] = "true"

        queryParams["seqn"] = pubnub.publishSequenceManager.nextSequence().toString()

        if (pubnub.configuration.isCipherKeyValid()) {
            stringifiedMessage = Crypto(pubnub.configuration.cipherKey)
                .encrypt(stringifiedMessage)
                .replace("\n", "")
        }

        if (usePost) {
            var payload = message

            if (pubnub.configuration.isCipherKeyValid()) {
                payload = stringifiedMessage
            }

            return pubnub.retrofitManager.publishService.publishWithPost(
                pubnub.configuration.publishKey,
                pubnub.configuration.subscribeKey,
                channel,
                payload,
                queryParams
            )
        } else {
            // get request

            if (pubnub.configuration.isCipherKeyValid()) {
                stringifiedMessage = "\"$stringifiedMessage\""
            }

            return pubnub.retrofitManager.publishService.publish(
                pubnub.configuration.publishKey,
                pubnub.configuration.subscribeKey,
                channel,
                stringifiedMessage,
                queryParams
            )
        }
    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult? {
        return PNPublishResult(
            timetoken = input.body()!![2].toString().toLong()
        )
    }

    override fun operationType() = PNOperationType.PNPublishOperation

    override fun isPubKeyRequired() = true
}
