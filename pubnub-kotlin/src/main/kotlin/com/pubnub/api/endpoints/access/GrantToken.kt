package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.access.GrantToken
import com.pubnub.internal.endpoints.access.IGrantToken

class GrantToken internal constructor(private val grantToken: GrantToken) : DelegatingEndpoint<PNGrantTokenResult>(),
    IGrantToken by grantToken {
    override fun createAction(): Endpoint<PNGrantTokenResult> = grantToken.mapIdentity()
}
