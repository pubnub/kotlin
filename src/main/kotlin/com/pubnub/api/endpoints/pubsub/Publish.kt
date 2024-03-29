package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.encryptString
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.extension.numericString
import com.pubnub.extension.quoted
import com.pubnub.extension.valueString
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.publish]
 */
class Publish internal constructor(
    pubnub: PubNub,
    val message: Any,
    val channel: String,
    val meta: Any? = null,
    val shouldStore: Boolean? = null,
    val usePost: Boolean = false,
    val replicate: Boolean = true,
    val ttl: Int? = null
) : Endpoint<List<Any>, PNPublishResult>(pubnub) {

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {

        addQueryParams(queryParams)

        return if (usePost) {
            val payload = getBodyMessage(message)

            pubnub.retrofitManager.publishService.publishWithPost(
                pubnub.configuration.publishKey,
                pubnub.configuration.subscribeKey,
                channel,
                payload,
                queryParams
            )
        } else {
            // HTTP GET request
            val stringifiedMessage = getParamMessage(message)

            pubnub.retrofitManager.publishService.publish(
                pubnub.configuration.publishKey,
                pubnub.configuration.subscribeKey,
                channel,
                stringifiedMessage,
                queryParams
            )
        }
    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult =
        PNPublishResult(
            timetoken = input.body()!![2].toString().toLong()
        )

    override fun operationType() = PNOperationType.PNPublishOperation

    override fun isPubKeyRequired() = true

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUBLISH

    // region Parameters
    /**
     * Add query params to passed HashMap
     *
     * @param queryParams hashMap to add parameters
     */
    private fun addQueryParams(queryParams: MutableMap<String, String>) {

        meta?.run { queryParams["meta"] = pubnub.mapper.toJson(this) }

        shouldStore?.run { queryParams["store"] = this.numericString }

        ttl?.run { queryParams["ttl"] = this.toString() }

        if (!replicate) queryParams["norep"] = true.valueString

        queryParams["seqn"] = pubnub.publishSequenceManager.nextSequence().toString()
    }
    // endregion

    // region Message parsers
    private fun getBodyMessage(message: Any): Any =
        pubnub.cryptoModule?.encryptString(toJson(message)) ?: message

    private fun getParamMessage(message: Any): String =
        pubnub.cryptoModule?.encryptString(toJson(message))?.quoted() ?: toJson(message)

    private fun toJson(message: Any): String = pubnub.mapper.toJson(message)
    // endregion
}
