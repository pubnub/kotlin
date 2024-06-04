package com.pubnub.api.endpoints.channel_groups

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeWithChannels
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.onSuccessReturnValue
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.removeChannelsFromChannelGroup]
 */
actual interface RemoveChannelChannelGroup : Endpoint<PNChannelGroupsRemoveChannelResult> {
}

@OptIn(ExperimentalForeignApi::class)
class RemoveChannelChannelGroupImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val channelGroup: String
): RemoveChannelChannelGroup {
    override fun async(callback: Consumer<Result<PNChannelGroupsRemoveChannelResult>>) {
        pubnub.removeWithChannels(
            channels = channels,
            from = channelGroup,
            onSuccess = callback.onSuccessHandler { PNChannelGroupsRemoveChannelResult() },
            onFailure = callback.onFailureHandler()
        )
    }
}