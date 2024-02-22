package com.pubnub.api.endpoints

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.ITime

/**
 * @see [PubNubImpl.time]
 */
class Time internal constructor(time: ITime) : ITime by time
