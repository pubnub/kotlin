package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.message_actions.AddMessageAction
import com.pubnub.internal.endpoints.message_actions.IAddMessageAction

/**
 * @see [PubNub.addMessageAction]
 */
class AddMessageAction internal constructor(private val addMessageAction: AddMessageAction) : DelegatingEndpoint<PNAddMessageActionResult>(),
    IAddMessageAction by addMessageAction {
    override fun createAction(): Endpoint<PNAddMessageActionResult> = addMessageAction.mapIdentity()
}