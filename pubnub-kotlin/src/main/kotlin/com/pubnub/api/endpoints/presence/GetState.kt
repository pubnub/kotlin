package com.pubnub.api.endpoints.presence

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.presence.IGetState

/**
 * @see [PubNub.getPresenceState]
 */
class GetState internal constructor(getState: IGetState) : IGetState by getState
