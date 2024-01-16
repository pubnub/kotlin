package com.pubnub.api.endpoints

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.IHistory

/**
 * @see [PubNub.history]
 */
class History internal constructor(history: IHistory) : IHistory by history
