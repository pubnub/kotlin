package com.pubnub.kmp.endpoints

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.timeOnSuccess
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.Time
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class TimeImpl(
    private val pubnub: PubNubObjC
): Time {
    override fun async(callback: Consumer<Result<PNTimeResult>>) {
        pubnub.timeOnSuccess(
            onSuccess = callback.onSuccessHandler { PNTimeResult(it.toLong()) },
            onFailure = callback.onFailureHandler()
        )
    }
}