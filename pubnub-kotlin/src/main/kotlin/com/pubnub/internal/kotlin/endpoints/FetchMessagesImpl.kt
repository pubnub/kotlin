package com.pubnub.internal.kotlin.endpoints

import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IFetchMessages

/**
 * @see [PubNubImpl.fetchMessages]
 */
class FetchMessagesImpl internal constructor(fetchMessages: IFetchMessages) : IFetchMessages by fetchMessages,
    FetchMessages
