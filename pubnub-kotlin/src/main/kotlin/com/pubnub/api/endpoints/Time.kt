package com.pubnub.api.endpoints

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.ITime

/**
 * @see [PubNub.time]
 */
class Time internal constructor(time: ITime) : ITime by time
