package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.access.Grant
import com.pubnub.internal.endpoints.access.IGrant

/**
 * @see [PubNub.grant]
 */
class Grant internal constructor(private val grant: Grant) :
    DelegatingEndpoint<PNAccessManagerGrantResult>(), IGrant by grant {

    override fun createAction(): Endpoint<PNAccessManagerGrantResult> =
        grant.map(PNAccessManagerGrantResult.Companion::from)
}
