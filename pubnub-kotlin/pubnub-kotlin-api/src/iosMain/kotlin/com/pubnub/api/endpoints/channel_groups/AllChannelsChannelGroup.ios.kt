package com.pubnub.api.endpoints.channel_groups

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.listChannelsFor
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
actual interface AllChannelsChannelGroup : Endpoint<PNChannelGroupsAllChannelsResult> {
}

@OptIn(ExperimentalForeignApi::class)
class AllChannelsChannelGroupImpl(
    private val pubnub: PubNubObjC,
    private val channelGroup: String
): AllChannelsChannelGroup {
    override fun async(callback: Consumer<Result<PNChannelGroupsAllChannelsResult>>) {
        pubnub.listChannelsFor(
            channelGroup = channelGroup,
            onSuccess = callback.onSuccessHandler { PNChannelGroupsAllChannelsResult(channels = it as List<String>) },
            onFailure = callback.onFailureHandler()
        )
    }
}