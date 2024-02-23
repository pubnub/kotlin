package com.pubnub.internal.kotlin.endpoints

import com.pubnub.api.endpoints.Time
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.ITime

/**
 * @see [PubNubImpl.time]
 */
class TimeImpl internal constructor(time: ITime) : ITime by time, Time
