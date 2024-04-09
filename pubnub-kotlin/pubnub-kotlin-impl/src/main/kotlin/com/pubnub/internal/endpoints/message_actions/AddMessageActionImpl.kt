package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.addMessageAction]
 */
class AddMessageActionImpl internal constructor(addMessageAction: AddMessageActionInterface) :
    AddMessageActionInterface by addMessageAction,
    AddMessageAction,
    EndpointImpl<PNAddMessageActionResult>(addMessageAction)
