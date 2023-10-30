package com.pubnub.api.endpoints.access

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.internal.endpoints.access.Grant
import com.pubnub.internal.endpoints.access.IGrant

/**
 * @see [PubNub.grant]
 */
class Grant internal constructor(grant: Grant) :
    DelegatingEndpoint<PNAccessManagerGrantResult, com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult>(
        grant
    ), IGrant by grant {
    override fun convertAction(remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult>): ExtendedRemoteAction<PNAccessManagerGrantResult> {
        return MappingRemoteAction(remoteAction, PNAccessManagerGrantResult.Companion::from)
    }
}
