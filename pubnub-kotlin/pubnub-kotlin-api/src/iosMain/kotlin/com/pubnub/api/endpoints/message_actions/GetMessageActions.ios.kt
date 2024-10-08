package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.KMPBoundedPage
import cocoapods.PubNubSwift.KMPMessageAction
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.getMessageActionsFrom
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
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
    private val pubnub: KMPPubNub,
    private val page: PNBoundedPage
) : GetMessageActions {
    override fun async(callback: Consumer<Result<PNGetMessageActionsResult>>) {
        pubnub.getMessageActionsFrom(
            channel = channel,
            page = KMPBoundedPage(
                start = page.start?.let { NSNumber(long = it) },
                end = page.end?.let { NSNumber(long = it) },
                limit = page.limit?.let { NSNumber(it) }
            ),
            onSuccess = callback.onSuccessHandler2 { messageActions, next ->
                PNGetMessageActionsResult(
                    actions = messageActions.filterAndMap { rawValue: KMPMessageAction -> createMessageAction(rawValue) }.toList(),
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

    private fun createMessageAction(rawValue: KMPMessageAction): PNMessageAction {
        return PNMessageAction(
            type = rawValue.actionType(),
            value = rawValue.actionValue(),
            messageTimetoken = rawValue.messageTimetoken().toLong()
        ).apply {
            actionTimetoken = rawValue.actionTimetoken().toLong()
            uuid = rawValue.publisher()
        }
    }
}
