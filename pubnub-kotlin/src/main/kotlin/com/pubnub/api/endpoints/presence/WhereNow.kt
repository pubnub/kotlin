package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.presence.IWhereNow
import com.pubnub.internal.endpoints.presence.WhereNow

/**
 * @see [PubNub.whereNow]
 */
class WhereNow internal constructor(private val whereNow: WhereNow) :
    DelegatingEndpoint<PNWhereNowResult>(),
    IWhereNow by whereNow {

    override fun createAction(): Endpoint<PNWhereNowResult> {
        return whereNow.map(PNWhereNowResult::from)
    }
}
