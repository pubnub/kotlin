package com.pubnub.api.endpoints.presence

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.presence.IHereNow

/**
 * @see [PubNub.hereNow]
 */
class HereNow internal constructor(hereNow: IHereNow) : IHereNow by hereNow
