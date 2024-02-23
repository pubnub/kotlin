package com.pubnub.internal.kotlin.endpoints

import com.pubnub.api.endpoints.History
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IHistory

/**
 * @see [PubNubImpl.history]
 */
class HistoryImpl internal constructor(history: IHistory) : IHistory by history, History
