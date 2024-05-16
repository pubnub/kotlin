package com.pubnub.api.endpoints

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.EndpointImpl
import com.pubnub.api.JsonElement
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import toMap

/**
 * @see [PubNub.fetchMessages]
 */
actual interface FetchMessages : Endpoint<PNFetchMessagesResult>

class FetchMessagesImpl(pubnub: PubNub, params: PubNub.FetchMessagesParameters) : FetchMessages,
    EndpointImpl<PubNub.FetchMessagesResponse, PNFetchMessagesResult>(promiseFactory = { pubnub.fetchMessages(params) },
        responseMapping = { response ->
            PNFetchMessagesResult(response.channels.toMap().mapValues {
                it.value.map { item ->
                    PNFetchMessageItem(
                        item.uuid,
                        item.message,
                        item.meta as JsonElement,
                        item.timetoken.toString().toLong(),
                        item.actions?.toMap()?.mapValues { entry: Map.Entry<String, PubNub.ActionContentToAction> ->
                            entry.value.toMap().mapValues { entry2: Map.Entry<String, Array<PubNub.Action>> ->
                                entry2.value.map { action ->
                                    PNFetchMessageItem.Action(action.uuid, action.actionTimetoken.toString())
                                }
                            }
                        },
                        HistoryMessageType.of(item.messageType?.toString()?.toInt()),
                        null, //TODO item.error
                    )
                }
            }, response.more?.let { PNBoundedPage(it.start.toLong(), null, it.max.toInt()) })
        })