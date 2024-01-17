package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.message_actions.GetMessageActions
import com.pubnub.internal.endpoints.message_actions.IGetMessageActions

/**
 * @see [PubNub.getMessageActions]
 */
class GetMessageActions internal constructor(private val getMessageActions: GetMessageActions) :
    DelegatingEndpoint<PNGetMessageActionsResult>(), IGetMessageActions by getMessageActions {
    override fun createAction(): Endpoint<PNGetMessageActionsResult> = getMessageActions.mapIdentity()
}