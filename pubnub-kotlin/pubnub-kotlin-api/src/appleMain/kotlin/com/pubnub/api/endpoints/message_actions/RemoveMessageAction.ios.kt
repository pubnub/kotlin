package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.removeMessageActionWithChannel
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessReturnValue
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.removeMessageAction]
 */
actual interface RemoveMessageAction : PNFuture<PNRemoveMessageActionResult>

@OptIn(ExperimentalForeignApi::class)
class RemoveMessageActionImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val messageTimetoken: Long,
    private val actionTimetoken: Long
) : RemoveMessageAction {
    override fun async(callback: Consumer<Result<PNRemoveMessageActionResult>>) {
        pubnub.removeMessageActionWithChannel(
            channel = channel,
            messageTimetoken = messageTimetoken.toULong(),
            actionTimetoken = actionTimetoken.toULong(),
            onSuccess = callback.onSuccessReturnValue(PNRemoveMessageActionResult()),
            onFailure = callback.onFailureHandler()
        )
    }
}
