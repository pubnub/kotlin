package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.getPresenceState]
 */
class GetStateImpl internal constructor(getState: GetStateInterface) :
    GetStateInterface by getState,
    GetState,
    EndpointImpl<PNGetStateResult>(getState)
