package com.pubnub.api.endpoints.message_actions

import cocoapods.PubNubSwift.PubNubBoundedPageObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getMessageActionsFrom
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.getMessageActions]
 */
actual interface GetMessageActions : Endpoint<PNGetMessageActionsResult> {
}

@OptIn(ExperimentalForeignApi::class)
class GetMessageActionsImpl(
    private val channel: String,
    private val pubnub: PubNubObjC,
    private val page: PNBoundedPage
): GetMessageActions {
    override fun async(callback: Consumer<Result<PNGetMessageActionsResult>>) {
        pubnub.getMessageActionsFrom(
            channel = channel,
            page = PubNubBoundedPageObjC(
                start = page.start?.let { NSNumber(long = it) },
                end = page.end?.let { NSNumber(long = it) },
                limit = page.limit?.let { NSNumber(it) }
            ),
            onSuccess = callback.onSuccessHandler {
                PNGetMessageActionsResult(
                    actions = emptyList(),
                    page = PNBoundedPage(
                        start = it?.next()?.start()?.longValue(),
                        end = it?.next()?.end()?.longValue(),
                        limit = it?.next()?.limit()?.intValue()
                    )
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}