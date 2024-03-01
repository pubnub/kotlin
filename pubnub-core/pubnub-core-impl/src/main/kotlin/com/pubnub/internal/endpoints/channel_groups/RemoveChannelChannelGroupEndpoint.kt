package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.removeChannelsFromChannelGroup]
 */
class RemoveChannelChannelGroupEndpoint internal constructor(
    pubnub: PubNubCore,
    override val channelGroup: String,
    override val channels: List<String>,
) : EndpointCore<Void, PNChannelGroupsRemoveChannelResult>(pubnub), IRemoveChannelChannelGroup {
    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun validateParams() {
        super.validateParams()
        if (channelGroup.isBlank()) throw PubNubException(PubNubError.GROUP_MISSING)
        if (channels.isEmpty()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        addQueryParams(queryParams)

        return pubnub.retrofitManager.channelGroupService
            .removeChannel(
                pubnub.configuration.subscribeKey,
                channelGroup,
                queryParams,
            )
    }

    override fun createResponse(input: Response<Void>): PNChannelGroupsRemoveChannelResult = PNChannelGroupsRemoveChannelResult()

    override fun operationType() = PNOperationType.PNRemoveChannelsFromGroupOperation

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (channels.isNotEmpty()) {
            queryParams["remove"] = channels.toCsv()
        }
    }

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
}
