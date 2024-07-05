package com.pubnub.kmp.endpoints.files

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.publishFileMessageWithChannel
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.files.PublishFileMessage
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
class PublishFileMessageImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val fileName: String,
    private val fileId: String,
    private val message: Any?,
    private val meta: Any?,
    private val ttl: Int?,
    private val shouldStore: Boolean?
): PublishFileMessage {
    override fun async(callback: Consumer<Result<PNPublishFileMessageResult>>) {
        pubnub.publishFileMessageWithChannel(
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            message = message,
            meta = meta,
            ttl = ttl?.let { NSNumber(it) },
            shouldStore = shouldStore?.let { NSNumber(bool = it) },
            onSuccess = callback.onSuccessHandler { PNPublishFileMessageResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
