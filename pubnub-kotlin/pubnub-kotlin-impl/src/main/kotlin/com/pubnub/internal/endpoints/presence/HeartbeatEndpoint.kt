package com.pubnub.internal.endpoints.presence

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.PubNubUtil
import retrofit2.Call
import retrofit2.Response

class HeartbeatEndpoint internal constructor(
    pubnub: PubNubImpl,
    val channels: List<String> = listOf(),
    val channelGroups: List<String> = listOf(),
    val state: Any? = null,
) : EndpointCore<Void, Boolean>(pubnub) {
    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        addQueryParams(queryParams)

        val channelsCsv =
            if (channels.isNotEmpty()) {
                channels.joinToString(",")
            } else {
                ","
            }

        return retrofitManager.presenceService.heartbeat(
            configuration.subscribeKey,
            channelsCsv,
            queryParams,
        )
    }

    private fun addQueryParams(queryParams: HashMap<String, String>) {
        queryParams["heartbeat"] = pubnub.configuration.presenceTimeout.toString()

        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.joinToString(",")
        }

        state?.let {
            queryParams["state"] = pubnub.mapper.toJson(it)
        }

        PubNubUtil.maybeAddEeQueryParam(queryParams)
    }

    override fun createResponse(input: Response<Void>): Boolean {
        return true
    }

    override fun operationType() = PNOperationType.PNHeartbeatOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE
}
