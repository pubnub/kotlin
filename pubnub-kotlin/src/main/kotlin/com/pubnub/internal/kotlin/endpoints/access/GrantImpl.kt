package com.pubnub.internal.kotlin.endpoints.access

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.access.Grant
import com.pubnub.internal.endpoints.access.IGrant

/**
 * @see [PubNubImpl.grant]
 */
class GrantImpl internal constructor(grant: Grant) :
    DelegatingEndpoint<PNAccessManagerGrantResult, com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult>(
        grant
    ),
    IGrant by grant,
    com.pubnub.api.endpoints.access.Grant {
    override fun convertAction(remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult>): ExtendedRemoteAction<PNAccessManagerGrantResult> {
        return MappingRemoteAction(remoteAction, PNAccessManagerGrantResult.Companion::from)
    }
}
