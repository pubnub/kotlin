package com.pubnub.api.endpoints

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.timeOnSuccess
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.time]
 */
actual interface Time : Endpoint<PNTimeResult>

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