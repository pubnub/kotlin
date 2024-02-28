package com.pubnub.internal.endpoints.presence

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult
import com.pubnub.internal.models.toApi

/**
 * @see [PubNubImpl.whereNow]
 */
class WhereNowImpl internal constructor(whereNow: WhereNowEndpoint) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.presence.PNWhereNowResult, PNWhereNowResult>(whereNow),
    WhereNowInterface by whereNow,
    com.pubnub.api.endpoints.presence.WhereNow {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<PNWhereNowResult>,
        ): ExtendedRemoteAction<com.pubnub.api.models.consumer.presence.PNWhereNowResult> {
            return MappingRemoteAction(
                remoteAction,
                PNWhereNowResult::toApi,
            )
        }
    }
