package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.presence.GetState
import com.pubnub.internal.endpoints.presence.IGetState

/**
 * @see [PubNub.getPresenceState]
 */
class GetState internal constructor(private val getState: GetState) : DelegatingEndpoint<PNGetStateResult>(), IGetState by getState {
    override fun createAction(): Endpoint<PNGetStateResult> = getState.mapIdentity()
}
