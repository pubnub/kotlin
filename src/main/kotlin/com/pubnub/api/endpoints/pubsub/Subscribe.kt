package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubUtil
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

class Subscribe(pubnub: PubNub) : Endpoint<SubscribeEnvelope, SubscribeEnvelope>(pubnub) {

    var channels = listOf<String>()
    var channelGroups = listOf<String>()
    var timetoken: Long? = null
    var region: String? = null
    var state: Any? = null
    var filterExpression: String? = null

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun doWork(queryParams: HashMap<String, String>): Call<SubscribeEnvelope> {
        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.joinToString(",")
        }

        if (!filterExpression.isNullOrBlank()) {
            queryParams["filter-expr"] = PubNubUtil.urlEncode(filterExpression!!)
        }

        timetoken?.let {
            queryParams["tt"] = it.toString()
        }

        region?.let {
            queryParams["tr"] = it
        }

        queryParams["heartbeat"] = pubnub.configuration.presenceTimeout.toString()

        state?.let {
            queryParams["state"] = PubNubUtil.urlEncode(pubnub.mapper.toJson(it))
        }

        queryParams.putAll(encodeParams(queryParams))

        return pubnub.retrofitManager.subscribeService.subscribe(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams
        )
    }

    override fun createResponse(input: Response<SubscribeEnvelope>): SubscribeEnvelope? {
        if (input.body() == null) {
            throw PubNubException(PubNubError.PARSING_ERROR)
        } else {
            return input.body()
        }
    }

    override fun operationType() = PNOperationType.PNSubscribeOperation

    override fun isSubKeyRequired() = true
    override fun isPubKeyRequired() = false
    override fun isAuthRequired() = true
}