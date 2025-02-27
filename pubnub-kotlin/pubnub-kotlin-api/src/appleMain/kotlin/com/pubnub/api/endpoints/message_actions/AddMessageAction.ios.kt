package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.addMessageActionWithChannel
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.addMessageAction]
 */
actual interface AddMessageAction : PNFuture<PNAddMessageActionResult>

@OptIn(ExperimentalForeignApi::class)
class AddMessageActionImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val actionType: String,
    private val actionValue: String,
    private val messageTimetoken: Long,
) : AddMessageAction {
    override fun async(callback: Consumer<Result<PNAddMessageActionResult>>) {
        pubnub.addMessageActionWithChannel(
            channel = channel,
            actionType = actionType,
            actionValue = actionValue,
            messageTimetoken = messageTimetoken.toULong(),
            onSuccess = callback.onSuccessHandler { messageActionObjC ->
                requireNotNull(messageActionObjC)
                PNAddMessageActionResult(
                    action = PNMessageAction(
                        type = messageActionObjC.actionType(),
                        value = messageActionObjC.actionValue(),
                        messageTimetoken = messageActionObjC.messageTimetoken().toLong(),
                    ).apply {
                        uuid = messageActionObjC.publisher()
                        actionTimetoken = messageActionObjC.actionTimetoken().toLong()
                    }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
