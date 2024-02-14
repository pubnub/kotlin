package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.internal.endpoints.message_actions.IAddMessageAction

/**
 * @see [PubNub.addMessageAction]
 */
class AddMessageAction internal constructor(addMessageAction: IAddMessageAction) :
    Endpoint<PNAddMessageActionResult>(),
    IAddMessageAction by addMessageAction
