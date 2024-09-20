package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.server.Envelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.listAllChannelGroups]
 */
class ListAllChannelGroupEndpoint internal constructor(pubnub: PubNubImpl) :
    EndpointCore<Envelope<Map<String, Any>>, PNChannelGroupsListAllResult>(pubnub),
    ListAllChannelGroup {
        override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Map<String, Any>>> {
            return retrofitManager.channelGroupService
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
