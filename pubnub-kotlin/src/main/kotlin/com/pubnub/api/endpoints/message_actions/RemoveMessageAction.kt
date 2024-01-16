package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.internal.endpoints.message_actions.IRemoveMessageAction

/**
 * @see [PubNub.removeMessageAction]
 */
class RemoveMessageAction internal constructor(removeMessageAction: IRemoveMessageAction) :
    Endpoint<PNRemoveMessageActionResult>(), IRemoveMessageAction by removeMessageAction