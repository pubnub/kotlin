package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.CoreEndpoint
import com.pubnub.internal.CorePubNubClient
import com.pubnub.internal.PubNubUtil
import com.pubnub.internal.models.server.SubscribeEnvelope
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response

class SubscribeEndpoint internal constructor(pubnub: CorePubNubClient) : CoreEndpoint<SubscribeEnvelope, SubscribeEnvelope>(pubnub) {
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
            queryParams,
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

    override fun createResponse(input: Response<SubscribeEnvelope>): SubscribeEnvelope {
        return input.body()!!
    }

    override fun operationType() = PNOperationType.PNSubscribeOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.SUBSCRIBE

    // it is excluded here because EE has dedicated logic for retry on Subscribe and Heartbeat
    override fun isEndpointRetryable(): Boolean = false
}
