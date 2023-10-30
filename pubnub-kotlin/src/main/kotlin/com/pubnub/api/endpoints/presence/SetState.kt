package com.pubnub.api.endpoints.presence

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.presence.ISetState

/**
 * @see [PubNub.setPresenceState]
 */
class SetState internal constructor(setState: ISetState) : ISetState by setState
