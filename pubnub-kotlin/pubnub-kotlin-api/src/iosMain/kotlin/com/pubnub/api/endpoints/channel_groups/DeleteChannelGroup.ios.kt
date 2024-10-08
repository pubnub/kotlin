package com.pubnub.api.endpoints.channel_groups

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.deleteWithChannelGroup
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.deleteChannelGroup]
 */
actual interface DeleteChannelGroup : PNFuture<PNChannelGroupsDeleteGroupResult>

@OptIn(ExperimentalForeignApi::class)
class DeleteChannelGroupImpl(
    private val pubnub: KMPPubNub,
    private val channelGroup: String
) : DeleteChannelGroup {
    override fun async(callback: Consumer<Result<PNChannelGroupsDeleteGroupResult>>) {
        pubnub.deleteWithChannelGroup(
            channelGroup = channelGroup,
            onSuccess = callback.onSuccessHandler { PNChannelGroupsDeleteGroupResult() },
            onFailure = callback.onFailureHandler()
        )
    }
}
