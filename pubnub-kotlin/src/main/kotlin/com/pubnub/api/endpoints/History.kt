package com.pubnub.api.endpoints

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IHistory

/**
 * @see [PubNubImpl.history]
 */
class History internal constructor(history: IHistory) : IHistory by history
