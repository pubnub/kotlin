package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.models.server.Envelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class AddChannelChannelGroup(pubnub: PubNub) : Endpoint<Envelope<Any>, PNChannelGroupsAddChannelResult>(pubnub) {

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

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Any>> {
        if (channels.isNotEmpty()) {
            queryParams["add"] = channels.toCsv()
        }

        return pubnub.retrofitManager.channelGroupService
            .addChannelChannelGroup(
                pubnub.configuration.subscribeKey,
                channelGroup,
                queryParams
            )
    }

    override fun createResponse(input: Response<Envelope<Any>>): PNChannelGroupsAddChannelResult? {
        return PNChannelGroupsAddChannelResult()
    }

    override fun operationType() = PNOperationType.PNAddChannelsToGroupOperation
}