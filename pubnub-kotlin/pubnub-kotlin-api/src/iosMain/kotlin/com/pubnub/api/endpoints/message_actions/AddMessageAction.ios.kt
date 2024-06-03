package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.addMessageActionWithChannel
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.addMessageAction]
 */
actual interface AddMessageAction : Endpoint<PNAddMessageActionResult> {
}

@OptIn(ExperimentalForeignApi::class)
class AddMessageActionImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val actionType: String,
    private val actionValue: String,
    private val messageTimetoken: Long
): AddMessageAction {
    override fun async(callback: Consumer<Result<PNAddMessageActionResult>>) {
        pubnub.addMessageActionWithChannel(
            channel = channel,
            actionType = actionType,
            actionValue = actionValue,
            messageTimetoken = messageTimetoken.toULong(),
            onSuccess = callback.onSuccessHandler { PNAddMessageActionResult(
                action = PNMessageAction(
                    type = it?.type() ?: "",
                    value = it?.value() ?: "",
                    messageTimetoken = it?.messageTimetoken()?.toLong() ?: 0
                )
            )}, onFailure = callback.onFailureHandler()
        )
    }
}