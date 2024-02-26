package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.setPresenceState]
 */
class SetStateImpl internal constructor(setState: ISetState) : ISetState by setState, SetState
