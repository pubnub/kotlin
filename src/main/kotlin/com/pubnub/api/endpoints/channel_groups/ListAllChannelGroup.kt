package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.models.server.Envelope
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

class ListAllChannelGroup(pubnub: PubNub) : Endpoint<Envelope<Map<String, Any>>, PNChannelGroupsListAllResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Map<String, Any>>> {
        return pubnub.retrofitManager.channelGroupService
            .listAllChannelGroup(
                pubnub.configuration.subscribeKey,
                queryParams
            )
    }

    override fun createResponse(input: Response<Envelope<Map<String, Any>>>): PNChannelGroupsListAllResult? {
        return PNChannelGroupsListAllResult(
            input.body()!!.payload!!["groups"] as List<String>
        )
    }

    override fun operationType() = PNOperationType.PNChannelGroupsOperation
}
