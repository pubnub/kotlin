package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.message_actions.IRemoveMessageAction
import com.pubnub.internal.endpoints.message_actions.RemoveMessageAction

/**
 * @see [PubNub.removeMessageAction]
 */
class RemoveMessageAction internal constructor(private val removeMessageAction: RemoveMessageAction) :
    DelegatingEndpoint<PNRemoveMessageActionResult>(), IRemoveMessageAction by removeMessageAction {
    override fun createAction(): Endpoint<PNRemoveMessageActionResult> = removeMessageAction.mapIdentity()
}