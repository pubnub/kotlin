package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.presence.HereNow
import com.pubnub.internal.endpoints.presence.IHereNow

/**
 * @see [PubNub.hereNow]
 */
class HereNow internal constructor(private val hereNow: HereNow) : DelegatingEndpoint<PNHereNowResult>(), IHereNow by hereNow {
    override fun createAction(): Endpoint<PNHereNowResult> = hereNow.mapIdentity()
}
