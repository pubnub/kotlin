package com.pubnub.api.endpoints.pubsub

import cocoapods.PubNubSwift.PubNubObjC
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.publish]
 */
actual interface Publish : Endpoint<PNPublishResult>

@OptIn(ExperimentalForeignApi::class)
open class PublishImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val message: Any,
    private val meta: Any?,
    private val shouldStore: Boolean?,
    private val ttl: Int?
): Publish {
    override fun async(callback: Consumer<Result<PNPublishResult>>) {
        pubnub.publishWithChannel(
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = if (shouldStore != null) NSNumber(bool = shouldStore) else null,
            ttl = if (ttl != null) NSNumber(int = ttl) else null,
            onResponse = callback.onSuccessHandler { PNPublishResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}