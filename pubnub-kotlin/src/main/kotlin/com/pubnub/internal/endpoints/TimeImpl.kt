package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.Time
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.time]
 */
class TimeImpl internal constructor(time: TimeInterface) : TimeInterface by time, Time
