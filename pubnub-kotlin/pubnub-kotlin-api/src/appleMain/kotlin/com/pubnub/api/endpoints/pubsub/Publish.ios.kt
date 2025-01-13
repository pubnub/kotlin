package com.pubnub.api.endpoints.pubsub

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.publishWithChannel
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.publish]
 */
actual interface Publish : PNFuture<PNPublishResult>

@OptIn(ExperimentalForeignApi::class)
class PublishImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val message: Any,
    private val meta: Any?,
    private val shouldStore: Boolean?,
    private val usePost: Boolean,
    private val ttl: Int?,
    private val customMessageType: String?
) : Publish {
    override fun async(callback: Consumer<Result<PNPublishResult>>) {
        pubnub.publishWithChannel(
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = shouldStore?.let { NSNumber(bool = it) },
            usePost = NSNumber(bool = usePost),
            ttl = ttl?.let { NSNumber(it) },
            customMessageType = customMessageType,
            onSuccess = callback.onSuccessHandler { PNPublishResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
