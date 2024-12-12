package com.pubnub.api.endpoints

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.PubNubError
import com.pubnub.api.createJsonElement
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.kmp.toMap

class FetchMessagesImpl(pubnub: PubNub, params: PubNub.FetchMessagesParameters) : FetchMessages,
    EndpointImpl<PubNub.FetchMessagesResponse, PNFetchMessagesResult>(
        promiseFactory = { pubnub.fetchMessages(params) },
        responseMapping = { response ->
            PNFetchMessagesResult(
                response.channels.toMap().mapValues { entry ->
                    entry.value.map { item ->
                        PNFetchMessageItem(
                            item.uuid,
                            createJsonElement(item.message),
                            item.meta?.let { createJsonElement(it) },
                            item.timetoken.toString().toLong(),
                            item.actions?.toMap()?.mapValues { entry: Map.Entry<String, PubNub.ActionContentToAction> ->
                                entry.value.toMap().mapValues { entry2: Map.Entry<String, Array<PubNub.Action>> ->
                                    entry2.value.map { action ->
                                        PNFetchMessageItem.Action(action.uuid, action.actionTimetoken.toLong())
                                    }
                                }
                            },
                            HistoryMessageType.of(item.messageType?.toString()?.toInt()),
                            if (item.error?.startsWith("Error while decrypting message content") ?: false) {
                                PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED
                            } else {
                                null
                            },
                            item.customMessageType
                        )
                    }
                },
                response.more?.let { PNBoundedPage(it.start.toLong(), null, it.max.toInt()) }
            )
        }
    )
