package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubUtil
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.policies.RetryableEndpointGroup
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

class Subscribe internal constructor(pubnub: PubNub) : Endpoint<SubscribeEnvelope, SubscribeEnvelope>(pubnub) {

    var channels = emptyList<String>()
    var channelGroups = emptyList<String>()
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
        addQueryParams(queryParams)

        return pubnub.retrofitManager.subscribeService.subscribe(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams
        )
    }

    private fun addQueryParams(queryParams: HashMap<String, String>) {
        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.joinToString(",")
        }

        if (!filterExpression.isNullOrBlank()) {
            queryParams["filter-expr"] = filterExpression!!
        }

        timetoken?.let {
            queryParams["tt"] = it.toString()
        }

        region?.let {
            queryParams["tr"] = it
        }

        queryParams["heartbeat"] = pubnub.configuration.presenceTimeout.toString()

        state?.let {
            queryParams["state"] = pubnub.mapper.toJson(it)
        }

        PubNubUtil.maybeAddEeQueryParam(pubnub.configuration, queryParams)
    }

    override fun createResponse(input: Response<SubscribeEnvelope>): SubscribeEnvelope? {
        return input.body()!!
    }

    override fun operationType() = PNOperationType.PNSubscribeOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.SUBSCRIBE
}
