package com.pubnub.kmp.endpoints.message_actions

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeMessageActionWithChannel
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessReturnValue
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.message_actions.RemoveMessageAction
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class RemoveMessageActionImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val messageTimetoken: Long,
    private val actionTimetoken: Long
): RemoveMessageAction {
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