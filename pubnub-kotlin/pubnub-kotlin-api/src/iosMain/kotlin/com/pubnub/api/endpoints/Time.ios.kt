package com.pubnub.api.endpoints

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.timeOnSuccess
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class TimeImpl(
    private val pubnub: KMPPubNub
) : Time {
    override fun async(callback: Consumer<Result<PNTimeResult>>) {
        pubnub.timeOnSuccess(
            onSuccess = callback.onSuccessHandler { PNTimeResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
