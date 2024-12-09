package com.pubnub.api.endpoints.pubsub

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.signalWithChannel
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class SignalImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val message: Any,
    private val customMessageType: String?,
) : Signal {
    override fun async(callback: Consumer<Result<PNPublishResult>>) {
        pubnub.signalWithChannel(
            channel = channel,
            message = message,
            customMessageType = customMessageType,
            onSuccess = callback.onSuccessHandler { PNPublishResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
