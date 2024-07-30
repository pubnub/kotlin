package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.PubNubBoundedPageObjC
import cocoapods.PubNubSwift.PubNubMessageActionObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getMessageActionsFrom
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.message_actions.PNSavedMessageAction
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.filterAndMap
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler2
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.getMessageActions]
 */
actual interface GetMessageActions : PNFuture<PNGetMessageActionsResult>

@OptIn(ExperimentalForeignApi::class)
class GetMessageActionsImpl(
    private val channel: String,
    private val pubnub: PubNubObjC,
    private val page: PNBoundedPage
) : GetMessageActions {
    override fun async(callback: Consumer<Result<PNGetMessageActionsResult>>) {
        pubnub.getMessageActionsFrom(
            channel = channel,
            page = PubNubBoundedPageObjC(
                start = page.start?.let { NSNumber(long = it) },
                end = page.end?.let { NSNumber(long = it) },
                limit = page.limit?.let { NSNumber(it) }
            ),
            onSuccess = callback.onSuccessHandler2 { messageActions, next ->
                PNGetMessageActionsResult(
                    actions = messageActions.filterAndMap { rawValue: PubNubMessageActionObjC -> createMessageAction(rawValue) }.toList(),
                    page = PNBoundedPage(
                        start = next?.start()?.longValue(),
                        end = next?.end()?.longValue(),
                        limit = next?.limit()?.intValue()
                    )
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }

    private fun createMessageAction(rawValue: PubNubMessageActionObjC): PNSavedMessageAction {
        return PNSavedMessageAction(
            type = rawValue.actionType(),
            value = rawValue.actionValue(),
            messageTimetoken = rawValue.messageTimetoken().toLong(),
            actionTimetoken = rawValue.actionTimetoken().toLong(),
            uuid = rawValue.publisher(),
        )
    }
}
