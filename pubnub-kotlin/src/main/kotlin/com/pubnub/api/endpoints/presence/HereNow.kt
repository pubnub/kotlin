package com.pubnub.api.endpoints.presence

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.presence.IHereNow

/**
 * @see [PubNubImpl.hereNow]
 */
class HereNow internal constructor(hereNow: IHereNow) : IHereNow by hereNow
