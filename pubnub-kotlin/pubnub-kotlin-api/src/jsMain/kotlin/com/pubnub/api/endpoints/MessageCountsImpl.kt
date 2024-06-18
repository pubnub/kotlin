package com.pubnub.api.endpoints

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.kmp.toMap

class MessageCountsImpl(pubnub: PubNub, params: PubNub.MessageCountsParameters) : MessageCounts,
    EndpointImpl<PubNub.MessageCountsResponse, PNMessageCountResult>(promiseFactory = { pubnub.messageCounts(params) },
        responseMapping = {
            PNMessageCountResult(it.channels.toMap().mapValues { entry -> entry.value.toLong() })
        })