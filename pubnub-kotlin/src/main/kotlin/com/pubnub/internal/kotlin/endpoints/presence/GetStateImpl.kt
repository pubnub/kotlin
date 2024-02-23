package com.pubnub.internal.kotlin.endpoints.presence

import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.presence.IGetState

/**
 * @see [PubNubImpl.getPresenceState]
 */
class GetStateImpl internal constructor(getState: IGetState) : IGetState by getState, GetState
