package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.internal.endpoints.message_actions.IGetMessageActions

/**
 * @see [PubNub.getMessageActions]
 */
class GetMessageActions internal constructor(getMessageActions: IGetMessageActions) :
    Endpoint<PNGetMessageActionsResult>(), IGetMessageActions by getMessageActions
