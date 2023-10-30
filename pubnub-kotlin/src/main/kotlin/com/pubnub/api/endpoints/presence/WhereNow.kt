package com.pubnub.api.endpoints.presence

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.endpoints.presence.IWhereNow
import com.pubnub.internal.endpoints.presence.WhereNow
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult

/**
 * @see [PubNub.whereNow]
 */
class WhereNow internal constructor(whereNow: WhereNow) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.presence.PNWhereNowResult, PNWhereNowResult>(whereNow),
    IWhereNow by whereNow {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNWhereNowResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.presence.PNWhereNowResult> {
        return MappingRemoteAction(
            remoteAction,
            com.pubnub.api.models.consumer.presence.PNWhereNowResult.Companion::from
        )
    }
}
