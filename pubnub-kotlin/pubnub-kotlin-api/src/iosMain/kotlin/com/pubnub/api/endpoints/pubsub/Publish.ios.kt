package com.pubnub.api.endpoints.pubsub

import cocoapods.PubNubSwift.PubNubObjC
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
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val message: Any,
    private val meta: Any?,
    private val shouldStore: Boolean?,
    private val ttl: Int?
) : Publish {
    override fun async(callback: Consumer<Result<PNPublishResult>>) {
        pubnub.publishWithChannel(
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = if (shouldStore != null) {
                NSNumber(bool = shouldStore)
            } else {
                null
            },
            ttl = if (ttl != null) {
                NSNumber(int = ttl)
            } else {
                null
            },
            onSuccess = callback.onSuccessHandler { PNPublishResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
