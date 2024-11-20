package com.pubnub.api.endpoints.files

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.publishFileMessageWithChannel
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.publishFileMessage]
 */
actual interface PublishFileMessage : PNFuture<PNPublishFileMessageResult>

@OptIn(ExperimentalForeignApi::class)
class PublishFileMessageImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val fileName: String,
    private val fileId: String,
    private val message: Any?,
    private val meta: Any?,
    private val ttl: Int?,
    private val shouldStore: Boolean?,
    private val customMessageType: String?
) : PublishFileMessage {
    override fun async(callback: Consumer<Result<PNPublishFileMessageResult>>) {
        pubnub.publishFileMessageWithChannel(
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            message = message,
            meta = meta,
            ttl = ttl?.let { NSNumber(it) },
            shouldStore = shouldStore?.let { NSNumber(bool = it) },
//            customMessageType = customMessageType, // todo ask Kuba
            onSuccess = callback.onSuccessHandler { PNPublishFileMessageResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
