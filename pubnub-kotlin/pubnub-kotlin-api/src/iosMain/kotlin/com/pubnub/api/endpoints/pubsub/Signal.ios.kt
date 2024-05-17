package com.pubnub.api.endpoints.pubsub

import cocoapods.PubNubSwift.PubNubObjC
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.signal]
 */
actual interface Signal : Endpoint<PNPublishResult>

@OptIn(ExperimentalForeignApi::class)
open class SignalImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val message: Any
): Signal {
    override fun async(callback: Consumer<Result<PNPublishResult>>) {
        pubnub.signalWithChannel(
            channel = channel,
            message = message,
            onResponse = callback.onSuccessHandler { PNPublishResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}