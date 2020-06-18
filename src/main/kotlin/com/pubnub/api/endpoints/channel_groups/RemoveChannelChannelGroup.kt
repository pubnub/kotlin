package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

class RemoveChannelChannelGroup(pubnub: PubNub) : Endpoint<Void, PNChannelGroupsRemoveChannelResult>(pubnub) {

    lateinit var channelGroup: String
    lateinit var channels: List<String>

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun validateParams() {
        super.validateParams()
        if (!::channelGroup.isInitialized || channelGroup.isBlank()) {
            throw PubNubException(PubNubError.GROUP_MISSING)
        }
        if (!::channels.isInitialized || channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        if (channels.isNotEmpty()) {
            queryParams["remove"] = channels.toCsv()
        }

        return pubnub.retrofitManager.channelGroupService
            .removeChannel(
                pubnub.configuration.subscribeKey,
                channelGroup,
                queryParams
            )
    }

    override fun createResponse(input: Response<Void>): PNChannelGroupsRemoveChannelResult? {
        return PNChannelGroupsRemoveChannelResult()
    }

    override fun operationType() = PNOperationType.PNRemoveChannelsFromGroupOperation
}
