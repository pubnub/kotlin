package com.pubnub.kmp.endpoints

import cocoapods.PubNubSwift.PubNubBoundedPageObjC
import cocoapods.PubNubSwift.PubNubMessageActionObjC
import cocoapods.PubNubSwift.PubNubMessageObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.fetchMessagesFrom
import com.pubnub.kmp.PNFuture
import com.pubnub.api.JsonElement
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.FetchMessages
import com.pubnub.kmp.safeCast
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
open class FetchMessagesImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val page: PNBoundedPage,
    private val includeUUID: Boolean,
    private val includeMeta: Boolean,
    private val includeMessageActions: Boolean,
    private val includeMessageType: Boolean
): FetchMessages {
    override fun async(callback: Consumer<Result<PNFetchMessagesResult>>) {
        pubnub.fetchMessagesFrom(
            channels = channels,
            includeUUID = includeUUID,
            includeMeta = includeMeta,
            includeMessageActions = includeMessageActions,
            includeMessageType = includeMessageType,
            page = PubNubBoundedPageObjC(
                start = page.start?.let { NSNumber(long = it) },
                end = page.end?.let { NSNumber(long = it) },
                limit =  page.limit?.let { NSNumber(int = it) }
            ),
            onSuccess = callback.onSuccessHandler {
                PNFetchMessagesResult(
                    channels = mapMessages(rawValue = it?.messages()),
                    page = PNBoundedPage(
                        start = it?.page()?.start()?.longValue,
                        end = it?.page()?.end()?.longValue,
                        limit = it?.page()?.limit()?.intValue
                    )
                )},
            onFailure = callback.onFailureHandler()
        )
    }

    private fun mapMessages(rawValue: Map<Any?, *>?): Map<String, List<PNFetchMessageItem>> {
        return (rawValue?.safeCast<String, List<PubNubMessageObjC>>())?.mapValues { entry ->
            entry.value.map {
                PNFetchMessageItem(
                    uuid = it.publisher(),
                    message = JsonElementImpl(it.payload()),
                    meta = it.metadata()?.let { obj -> JsonElementImpl(obj) },
                    timetoken = it.published().toLong(),
                    actions = mapMessageActions(rawValue = it.actions()),
                    messageType = HistoryMessageType.of(it.messageType().toInt()),
                    error = null //todo translate crypto errors
                )
            }
        } ?: emptyMap()
    }

    private fun mapMessageActions(rawValue: List<*>): Map<String, Map<String, List<PNFetchMessageItem.Action>>>? {
        return rawValue.filterIsInstance<PubNubMessageActionObjC>().groupBy { messageAction ->
            messageAction.actionType()
        }.mapValues { entry ->
            entry.value.groupBy { groupedMessageAction ->
                groupedMessageAction.actionValue()
            }.mapValues { groupedEntry ->
                groupedEntry.value.map {
                    PNFetchMessageItem.Action(
                        uuid = it.publisher(),
                        actionTimetoken = it.published()?.longValue!!
                    )
                }
            }
        }.ifEmpty {
            null
        }
    }
}