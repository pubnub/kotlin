package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.addMessageActionWithChannel
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.addMessageAction]
 */
actual interface AddMessageAction : PNFuture<PNAddMessageActionResult> {
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
            onSuccess = callback.onSuccessHandler {   PNAddMessageActionResult(
                action = PNMessageAction(
                    type = it?.actionType().orEmpty(),
                    value = it?.actionValue().orEmpty(),
                    messageTimetoken = it?.messageTimetoken()?.toLong() ?: 0,
                ).apply {
                    uuid = it?.publisher()
                    actionTimetoken = it?.actionTimetoken()?.toLong()
                }
            )}, onFailure = callback.onFailureHandler()
        )
    }
}