package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.getMessageActions]
 */
class GetMessageActionsImpl internal constructor(getMessageActions: IGetMessageActions) :
    IGetMessageActions by getMessageActions,
    GetMessageActions
