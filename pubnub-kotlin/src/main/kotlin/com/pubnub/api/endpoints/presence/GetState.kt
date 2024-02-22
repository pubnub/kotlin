package com.pubnub.api.endpoints.presence

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.presence.IGetState

/**
 * @see [PubNubImpl.getPresenceState]
 */
class GetState internal constructor(getState: IGetState) : IGetState by getState
