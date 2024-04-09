package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.fetchMessages]
 */
class FetchMessagesImpl internal constructor(fetchMessages: FetchMessagesInterface) :
    FetchMessagesInterface by fetchMessages,
    FetchMessages,
    EndpointImpl<PNFetchMessagesResult>(fetchMessages)
