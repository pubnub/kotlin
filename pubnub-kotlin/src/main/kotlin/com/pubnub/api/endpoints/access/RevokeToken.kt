package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.access.RevokeToken

class RevokeToken internal constructor(private val token: RevokeToken) : DelegatingEndpoint<Unit>() {
    override fun createAction(): Endpoint<Unit> = token.mapIdentity()
}
