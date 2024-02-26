package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.History
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.history]
 */
class HistoryImpl internal constructor(history: IHistory) : IHistory by history, History
