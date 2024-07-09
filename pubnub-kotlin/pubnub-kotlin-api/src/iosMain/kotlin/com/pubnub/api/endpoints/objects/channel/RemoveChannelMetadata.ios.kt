package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeChannelMetadataWithChannel
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

actual interface RemoveChannelMetadata : PNFuture<PNRemoveMetadataResult>

@OptIn(ExperimentalForeignApi::class)
class RemoveChannelMetadataImpl(
    private val pubnub: PubNubObjC,
    private val channel: String
) : RemoveChannelMetadata {
    override fun async(callback: Consumer<Result<PNRemoveMetadataResult>>) {
        pubnub.removeChannelMetadataWithChannel(
            channel = channel,
            onSuccess = callback.onSuccessHandler { PNRemoveMetadataResult(200) },
            onFailure = callback.onFailureHandler()
        )
    }
}
