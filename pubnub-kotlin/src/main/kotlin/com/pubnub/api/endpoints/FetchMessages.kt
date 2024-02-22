package com.pubnub.api.endpoints

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IFetchMessages

/**
 * @see [PubNubImpl.fetchMessages]
 */
class FetchMessages internal constructor(fetchMessages: IFetchMessages) : IFetchMessages by fetchMessages
