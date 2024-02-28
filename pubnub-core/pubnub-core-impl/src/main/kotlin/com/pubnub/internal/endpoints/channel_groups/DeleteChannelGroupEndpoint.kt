package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.CoreEndpoint
import com.pubnub.internal.CorePubNubClient
import retrofit2.Call
import retrofit2.Response

/**
 * @see [CorePubNubClient.deleteChannelGroup]
 */
class DeleteChannelGroupEndpoint internal constructor(
    pubnub: CorePubNubClient,
    override val channelGroup: String,
) : CoreEndpoint<Void, PNChannelGroupsDeleteGroupResult>(pubnub), DeleteChannelGroupInterface {
    override fun validateParams() {
        super.validateParams()
        if (channelGroup.isBlank()) throw PubNubException(PubNubError.GROUP_MISSING)
    }

    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        return pubnub.retrofitManager.channelGroupService
            .deleteChannelGroup(
                pubnub.configuration.subscribeKey,
                channelGroup,
                queryParams,
            )
    }

    override fun createResponse(input: Response<Void>): PNChannelGroupsDeleteGroupResult = PNChannelGroupsDeleteGroupResult()

    override fun operationType() = PNOperationType.PNRemoveGroupOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
}
