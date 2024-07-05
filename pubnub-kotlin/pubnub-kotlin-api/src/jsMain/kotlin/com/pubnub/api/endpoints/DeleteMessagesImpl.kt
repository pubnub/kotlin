package com.pubnub.kmp.endpoints

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.kmp.toMap

class DeleteMessagesImpl(pubnub: PubNub, params: PubNub.DeleteMessagesParameters) : DeleteMessages,
    EndpointImpl<Unit, PNDeleteMessagesResult>(promiseFactory = { pubnub.deleteMessages(params) },
        responseMapping = {
            PNDeleteMessagesResult()
        })