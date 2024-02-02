package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.models.server.Envelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroup internal constructor(
    pubnub: PubNub,
    val channelGroup: String
) : Endpoint<Envelope<Map<String, Any>>, PNChannelGroupsAllChannelsResult>(pubnub) {

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
}
