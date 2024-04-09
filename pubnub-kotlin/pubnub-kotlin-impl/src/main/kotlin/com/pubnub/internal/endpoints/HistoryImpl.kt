package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.History
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.history]
 */
class HistoryImpl internal constructor(history: HistoryInterface) :
    HistoryInterface by history,
    History,
    EndpointImpl<PNHistoryResult>(history)
