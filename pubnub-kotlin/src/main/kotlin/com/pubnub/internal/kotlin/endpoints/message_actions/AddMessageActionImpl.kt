package com.pubnub.internal.kotlin.endpoints.message_actions

import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.message_actions.IAddMessageAction

/**
 * @see [PubNubImpl.addMessageAction]
 */
class AddMessageActionImpl internal constructor(addMessageAction: IAddMessageAction) :
    IAddMessageAction by addMessageAction,
        AddMessageAction
