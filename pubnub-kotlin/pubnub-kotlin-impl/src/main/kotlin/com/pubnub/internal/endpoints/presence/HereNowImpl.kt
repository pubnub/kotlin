package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.hereNow]
 */
class HereNowImpl internal constructor(hereNow: HereNowInterface) :
    HereNowInterface by hereNow,
    HereNow,
    EndpointImpl<PNHereNowResult>(hereNow)
