package com.pubnub.api.endpoints.channel_groups

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.deleteWithChannelGroup
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.deleteChannelGroup]
 */
actual interface DeleteChannelGroup : Endpoint<PNChannelGroupsDeleteGroupResult>

@OptIn(ExperimentalForeignApi::class)
class DeleteChannelGroupImpl(
    private val pubnub: PubNubObjC,
    private val channelGroup: String
): DeleteChannelGroup {
    override fun async(callback: Consumer<Result<PNChannelGroupsDeleteGroupResult>>) {
        pubnub.deleteWithChannelGroup(
            channelGroup = channelGroup,
            onSuccess = callback.onSuccessHandler { PNChannelGroupsDeleteGroupResult() },
            onFailure = callback.onFailureHandler()
        )
    }
}