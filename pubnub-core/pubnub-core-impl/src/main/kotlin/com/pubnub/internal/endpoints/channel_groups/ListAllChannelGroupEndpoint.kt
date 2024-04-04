package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.models.server.Envelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.listAllChannelGroups]
 */
class ListAllChannelGroupEndpoint internal constructor(pubnub: PubNubCore) :
    EndpointCore<Envelope<Map<String, Any>>, PNChannelGroupsListAllResult>(pubnub),
    ListAllChannelGroupInterface {
        override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Map<String, Any>>> {
            return pubnub.retrofitManager.channelGroupService
                .listAllChannelGroup(
                    configuration.subscribeKey,
                    queryParams,
                )
        }

        @Suppress("UNCHECKED_CAST")
        override fun createResponse(input: Response<Envelope<Map<String, Any>>>): PNChannelGroupsListAllResult =
            PNChannelGroupsListAllResult(
                groups = input.body()!!.payload!!["groups"] as List<String>,
            )

        override fun operationType() = PNOperationType.PNChannelGroupsOperation

        override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
    }
