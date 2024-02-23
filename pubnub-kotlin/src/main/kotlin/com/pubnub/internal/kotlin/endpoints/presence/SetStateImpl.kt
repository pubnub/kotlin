package com.pubnub.internal.kotlin.endpoints.presence

import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.presence.ISetState

/**
 * @see [PubNubImpl.setPresenceState]
 */
class SetStateImpl internal constructor(setState: ISetState) : ISetState by setState, SetState
