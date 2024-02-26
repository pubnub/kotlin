package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.fetchMessages]
 */
class FetchMessagesImpl internal constructor(fetchMessages: IFetchMessages) :
    IFetchMessages by fetchMessages,
    FetchMessages
