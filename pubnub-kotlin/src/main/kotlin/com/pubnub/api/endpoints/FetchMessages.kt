package com.pubnub.api.endpoints

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.IFetchMessages

/**
 * @see [PubNub.fetchMessages]
 */
class FetchMessages internal constructor(fetchMessages: IFetchMessages) : IFetchMessages by fetchMessages
