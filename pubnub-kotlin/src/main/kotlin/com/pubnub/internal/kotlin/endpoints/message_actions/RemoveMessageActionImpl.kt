package com.pubnub.internal.kotlin.endpoints.message_actions

import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.message_actions.IRemoveMessageAction

/**
 * @see [PubNubImpl.removeMessageAction]
 */
class RemoveMessageActionImpl internal constructor(removeMessageAction: IRemoveMessageAction) :
    IRemoveMessageAction by removeMessageAction,
        RemoveMessageAction
