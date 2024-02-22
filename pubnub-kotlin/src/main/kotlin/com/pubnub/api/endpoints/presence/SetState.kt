package com.pubnub.api.endpoints.presence

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.presence.ISetState

/**
 * @see [PubNubImpl.setPresenceState]
 */
class SetState internal constructor(setState: ISetState) : ISetState by setState
