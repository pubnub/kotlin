package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.api.models.server.Envelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class DeleteChannelGroup(pubnub: PubNub) : Endpoint<Envelope<Any>, PNChannelGroupsDeleteGroupResult>(pubnub) {

    lateinit var channelGroup: String

    override fun validateParams() {
        super.validateParams()
        if (!::channelGroup.isInitialized || channelGroup.isBlank()) {
            throw PubNubException(PubNubError.GROUP_MISSING)
        }
    }

    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Any>> {
        return pubnub.retrofitManager.channelGroupService
            .deleteChannelGroup(
                pubnub.configuration.subscribeKey,
                channelGroup,
                queryParams
            )
    }

    override fun createResponse(input: Response<Envelope<Any>>): PNChannelGroupsDeleteGroupResult? {
        if (input.body() == null) {
            throw PubNubException(PubNubError.PARSING_ERROR)
        }
        return PNChannelGroupsDeleteGroupResult()
    }

    override fun operationType() = PNOperationType.PNRemoveGroupOperation
}