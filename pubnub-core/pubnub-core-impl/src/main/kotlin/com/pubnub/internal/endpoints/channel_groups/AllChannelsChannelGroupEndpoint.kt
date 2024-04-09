package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.models.server.Envelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroupEndpoint internal constructor(
    pubnub: PubNubCore,
    override val channelGroup: String,
) : EndpointCore<Envelope<Map<String, Any>>, PNChannelGroupsAllChannelsResult>(pubnub),
    AllChannelsChannelGroupInterface {
    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun validateParams() {
        super.validateParams()
        if (channelGroup.isBlank()) {
            throw PubNubException(PubNubError.GROUP_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Map<String, Any>>> {
        return retrofitManager.channelGroupService
            .allChannelsChannelGroup(
                configuration.subscribeKey,
                channelGroup,
                queryParams,
            )
    }

    @Suppress("UNCHECKED_CAST")
    override fun createResponse(input: Response<Envelope<Map<String, Any>>>): PNChannelGroupsAllChannelsResult =
        PNChannelGroupsAllChannelsResult(
            channels = input.body()!!.payload!!["channels"] as List<String>,
        )

    override fun operationType() = PNOperationType.PNChannelsForGroupOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
}
