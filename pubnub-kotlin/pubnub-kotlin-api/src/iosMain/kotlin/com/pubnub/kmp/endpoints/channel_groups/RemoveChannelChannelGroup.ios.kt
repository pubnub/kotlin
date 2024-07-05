package com.pubnub.kmp.endpoints.channel_groups

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeWithChannels
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.channel_groups.RemoveChannelChannelGroup
import kotlinx.cinterop.ExperimentalForeignApi

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