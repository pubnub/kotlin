package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.addMessageAction]
 */
class AddMessageActionImpl internal constructor(addMessageAction: AddMessageActionInterface) :
    AddMessageActionInterface by addMessageAction,
    AddMessageAction
