package com.pubnub.api.endpoints.pubsub

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.signalWithChannel
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.signal]
 */
actual interface Signal : PNFuture<PNPublishResult>

@OptIn(ExperimentalForeignApi::class)
class SignalImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val message: Any
) : Signal {
    override fun async(callback: Consumer<Result<PNPublishResult>>) {
        pubnub.signalWithChannel(
            channel = channel,
            message = message,
            onSuccess = callback.onSuccessHandler { PNPublishResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
