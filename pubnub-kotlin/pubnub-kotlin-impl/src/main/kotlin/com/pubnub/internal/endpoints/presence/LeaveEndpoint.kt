package com.pubnub.internal.endpoints.presence

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.PubNubUtil
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response

class LeaveEndpoint internal constructor(pubnub: PubNubImpl) : EndpointCore<Void, Boolean>(pubnub) {
    var channels = emptyList<String>()
    var channelGroups = emptyList<String>()

    override fun validateParams() {
        super.validateParams()
        when {
            channels.any { it.isEmpty() } || channelGroups.any { it.isEmpty() } -> {
                throw PubNubException(PubNubError.CHANNEL_AND_GROUP_CONTAINS_EMPTY_STRING)
            }
            channels.isEmpty() && channelGroups.isEmpty() -> {
                throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
            }
        }
    }

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        addQueryParams(queryParams)
        return retrofitManager.presenceService.leave(
            configuration.subscribeKey,
            channels.toCsv(),
            queryParams,
        )
    }

    private fun addQueryParams(queryParams: HashMap<String, String>) {
        queryParams["channel-group"] = channelGroups.toCsv()
        PubNubUtil.maybeAddEeQueryParam(queryParams)
    }

    override fun createResponse(input: Response<Void>) = true

    override fun operationType() = PNOperationType.PNUnsubscribeOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE
}
