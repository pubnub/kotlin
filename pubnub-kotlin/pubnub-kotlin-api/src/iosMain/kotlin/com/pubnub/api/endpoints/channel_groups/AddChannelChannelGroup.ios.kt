package com.pubnub.api.endpoints.channel_groups

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.addChannelsTo
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
actual interface AddChannelChannelGroup : PNFuture<PNChannelGroupsAddChannelResult>

@OptIn(ExperimentalForeignApi::class)
class AddChannelChannelGroupImpl(
    private val pubnub: KMPPubNub,
    private val channels: List<String>,
    private val channelGroup: String
) : AddChannelChannelGroup {
    override fun async(callback: Consumer<Result<PNChannelGroupsAddChannelResult>>) {
        pubnub.addChannelsTo(
            channelGroup = channelGroup,
            channels = channels,
            onSuccess = callback.onSuccessHandler { PNChannelGroupsAddChannelResult() },
            onFailure = callback.onFailureHandler()
        )
    }
}
