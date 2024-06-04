package com.pubnub.api.endpoints.channel_groups

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.listChannelGroupsOnSuccess
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.listAllChannelGroups]
 */
actual interface ListAllChannelGroup : Endpoint<PNChannelGroupsListAllResult>

@OptIn(ExperimentalForeignApi::class)
class ListAllChannelGroupImpl(
    private val pubnub: PubNubObjC
): ListAllChannelGroup {
    override fun async(callback: Consumer<Result<PNChannelGroupsListAllResult>>) {
        pubnub.listChannelGroupsOnSuccess(
            onSuccess = callback.onSuccessHandler { PNChannelGroupsListAllResult(groups = it as List<String>) },
            onFailure = callback.onFailureHandler()
        )
    }
}