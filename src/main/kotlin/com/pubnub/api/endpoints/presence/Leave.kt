package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubUtil
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response

class Leave internal constructor(pubnub: PubNub) : Endpoint<Void, Boolean>(pubnub) {

    var channels = emptyList<String>()
    var channelGroups = emptyList<String>()

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun getAffectedChannels() = channels
    override fun getAffectedChannelGroups() = channelGroups

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        addQueryParams(queryParams)
        return pubnub.retrofitManager.presenceService.leave(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams
        )
    }

    private fun addQueryParams(queryParams: HashMap<String, String>) {
        queryParams["channel-group"] = channelGroups.toCsv()
        PubNubUtil.maybeAddEeQueryParam(pubnub.configuration, queryParams)
    }

    override fun createResponse(input: Response<Void>) = true

    override fun operationType() = PNOperationType.PNUnsubscribeOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE
}
