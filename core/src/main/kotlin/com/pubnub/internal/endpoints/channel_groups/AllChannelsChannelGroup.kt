package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.PubNub
import com.pubnub.internal.models.server.Envelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroup internal constructor(
    pubnub: PubNub,
    override val channelGroup: String
) : com.pubnub.internal.Endpoint<Envelope<Map<String, Any>>, PNChannelGroupsAllChannelsResult>(pubnub),
    IAllChannelsChannelGroup {

    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun validateParams() {
        super.validateParams()
        if (channelGroup.isBlank()) throw PubNubException(PubNubError.GROUP_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Map<String, Any>>> {
        return pubnub.retrofitManager.channelGroupService
            .allChannelsChannelGroup(
                pubnub.configuration.subscribeKey,
                channelGroup,
                queryParams
            )
    }

    @Suppress("UNCHECKED_CAST")
    override fun createResponse(input: Response<Envelope<Map<String, Any>>>): PNChannelGroupsAllChannelsResult =
        PNChannelGroupsAllChannelsResult(
            channels = input.body()!!.payload!!["channels"] as List<String>
        )

    override fun operationType() = PNOperationType.PNChannelsForGroupOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
}
