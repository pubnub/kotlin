package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response

class Subscribe internal constructor(pubnub: PubNub) : Endpoint<SubscribeEnvelope, SubscribeEnvelope>(pubnub) {

    companion object {
        internal const val INCLUDE_SPACE_ID_QUERY_PARAM = "include-space-id"
        internal const val INCLUDE_MESSAGE_TYPE_QUERY_PARAM = "include-type"
    }

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

        if (pubnub.configuration.includeMessageType) queryParams[INCLUDE_MESSAGE_TYPE_QUERY_PARAM] =
            pubnub.configuration.includeMessageType.toString()

        if (pubnub.configuration.includeSpaceId) queryParams[INCLUDE_SPACE_ID_QUERY_PARAM] =
            pubnub.configuration.includeSpaceId.toString()

        return pubnub.retrofitManager.subscribeService.subscribe(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams
        )
    }

    override fun createResponse(input: Response<SubscribeEnvelope>): SubscribeEnvelope? {
        return input.body()!!
    }

    override fun operationType() = PNOperationType.PNSubscribeOperation
}
