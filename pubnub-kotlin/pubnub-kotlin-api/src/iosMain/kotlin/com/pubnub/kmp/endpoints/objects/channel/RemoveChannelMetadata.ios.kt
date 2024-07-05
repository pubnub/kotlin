package com.pubnub.kmp.endpoints.objects.channel

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeChannelMetadataWithChannel
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.objects.channel.RemoveChannelMetadata
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class RemoveChannelMetadataImpl(
    private val pubnub: PubNubObjC,
    private val channel: String
): RemoveChannelMetadata {
    override fun async(callback: Consumer<Result<PNRemoveMetadataResult>>) {
        pubnub.removeChannelMetadataWithChannel(
            channel = channel,
            onSuccess = callback.onSuccessHandler { PNRemoveMetadataResult(200) },
            onFailure = callback.onFailureHandler()
        )
    }
}