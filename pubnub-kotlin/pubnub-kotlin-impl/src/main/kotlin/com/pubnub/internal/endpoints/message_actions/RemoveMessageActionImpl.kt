package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.removeMessageAction]
 */
class RemoveMessageActionImpl internal constructor(removeMessageAction: RemoveMessageActionInterface) :
    RemoveMessageActionInterface by removeMessageAction,
    RemoveMessageAction,
    EndpointImpl<PNRemoveMessageActionResult>(removeMessageAction)
