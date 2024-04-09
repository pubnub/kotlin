package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.getMessageActions]
 */
class GetMessageActionsImpl internal constructor(getMessageActions: GetMessageActionsInterface) :
    GetMessageActionsInterface by getMessageActions,
    GetMessageActions,
    EndpointImpl<PNGetMessageActionsResult>(getMessageActions)
