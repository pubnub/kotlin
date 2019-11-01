package com.pubnub.api.endpoints

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.vendor.Crypto
import retrofit2.Call
import retrofit2.Response
import java.util.*

class Publish(pubnub: PubNub) : Endpoint<List<Any>, PNPublishResult>(pubnub) {

    inner class Params {
        var channel: String? = null
        var message: Any? = null
        var shouldStore: Boolean = false
        var usePost: Boolean = false
        var meta: Any? = null
        var replicate = true
        var ttl: Int? = null
    }

    val params = Params()

    override fun validateParams() {
        super.validateParams()
        if (params.channel.isNullOrBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (params.message == null) {
            throw PubNubException(PubNubError.MESSAGE_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(params.channel)

    override fun getAffectedChannelGroups() = emptyList<String>()

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {

        var stringifiedMessage = pubnub.mapper.toJson(params.message!!)

        params.meta?.let {
            val stringifiedMeta = pubnub.mapper.toJson(it)
            PubNubUtil.urlEncode(stringifiedMeta)
            queryParams["meta"] = stringifiedMeta
        }


        if (params.shouldStore) {
            queryParams["store"] = "1"
        }

        params.ttl?.let {
            queryParams["ttl"] = it.toString()
        }

        when {
            !params.replicate -> queryParams["norep"] = "true"
        }

        queryParams["seqn"] = pubnub.publishSequenceManager.nextSequence().toString()

        queryParams.putAll(encodeParams(queryParams))

        pubnub.config.cipherKey?.let {
            stringifiedMessage = Crypto(it).encrypt(stringifiedMessage).replace("\n", "")
        }

        if (params.usePost) {
            var payload = params.message

            pubnub.config.cipherKey?.let {
                payload = stringifiedMessage
            }

            return pubnub.retrofitManager.publishService.publishWithPost(
                pubnub.config.publishKey!!,
                pubnub.config.subscribeKey!!,
                params.channel!!,
                payload!!,
                queryParams
            )
        } else {
            pubnub.config.cipherKey?.let {
                stringifiedMessage = "\"$stringifiedMessage\""
            }

            stringifiedMessage = PubNubUtil.urlEncode(stringifiedMessage)

            return pubnub.retrofitManager.publishService.publish(
                pubnub.config.publishKey!!,
                pubnub.config.subscribeKey!!,
                params.channel!!,
                stringifiedMessage,
                queryParams
            )
        }


    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult? {
        return input.body()?.let {
            PNPublishResult(it[2].toString().toLong())
        }
    }

    override fun operationType() = PNOperationType.PNPublishOperation

    override fun isSubKeyRequired() = true
    override fun isPubKeyRequired() = true
    override fun isAuthRequired() = true

}