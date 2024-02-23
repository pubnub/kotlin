package com.pubnub.internal.kotlin.endpoints.presence

import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.presence.IHereNow

/**
 * @see [PubNubImpl.hereNow]
 */
class HereNowImpl internal constructor(hereNow: IHereNow) : IHereNow by hereNow, HereNow
