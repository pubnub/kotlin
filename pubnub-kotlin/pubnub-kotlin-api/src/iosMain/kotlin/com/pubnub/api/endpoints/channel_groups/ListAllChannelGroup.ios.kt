package com.pubnub.api.endpoints.channel_groups

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.listChannelGroupsOnSuccess
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.listAllChannelGroups]
 */
actual interface ListAllChannelGroup : PNFuture<PNChannelGroupsListAllResult>

@OptIn(ExperimentalForeignApi::class)
class ListAllChannelGroupImpl(
    private val pubnub: KMPPubNub
) : ListAllChannelGroup {
    override fun async(callback: Consumer<Result<PNChannelGroupsListAllResult>>) {
        pubnub.listChannelGroupsOnSuccess(
            onSuccess = callback.onSuccessHandler { PNChannelGroupsListAllResult(groups = it?.filterIsInstance<String>() ?: emptyList()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
