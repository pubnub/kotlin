package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.presence.ISetState
import com.pubnub.internal.endpoints.presence.SetState

/**
 * @see [PubNub.setPresenceState]
 */
class SetState internal constructor(private val setState: SetState) : DelegatingEndpoint<PNSetStateResult>(), ISetState by setState {
    override fun createAction(): Endpoint<PNSetStateResult> = setState.mapIdentity()
}
