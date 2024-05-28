package com.pubnub.api.endpoints

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.deleteMessagesFrom
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessReturnValue
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [com.pubnub.api.PubNub.deleteMessages]
 */
actual interface DeleteMessages : Endpoint<PNDeleteMessagesResult>

@OptIn(ExperimentalForeignApi::class)
class DeleteMessagesImpl (
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val start: Long?,
    private val end: Long?
): DeleteMessages {
    override fun async(callback: Consumer<Result<PNDeleteMessagesResult>>) {
        pubnub.deleteMessagesFrom(
            channels = channels,
            start = start?.let { NSNumber(long = it) },
            end = end?.let { NSNumber(long = it) },
            onSuccess = callback.onSuccessReturnValue(PNDeleteMessagesResult()),
            onFailure = callback.onFailureHandler()
        )
    }
}