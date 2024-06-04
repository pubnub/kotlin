package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeMessageActionWithChannel
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessReturnValue
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.removeMessageAction]
 */
actual interface RemoveMessageAction : Endpoint<PNRemoveMessageActionResult> {
}

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