package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.Time
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.time]
 */
class TimeImpl internal constructor(time: TimeInterface) :
    TimeInterface by time,
    Time,
    EndpointImpl<PNTimeResult>(time)
