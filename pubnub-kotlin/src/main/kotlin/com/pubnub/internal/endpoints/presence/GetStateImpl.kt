package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.getPresenceState]
 */
class GetStateImpl internal constructor(getState: GetStateInterface) : GetStateInterface by getState, GetState
