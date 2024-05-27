package com.pubnub.api.endpoints.message_actions

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.kmp.toMessageAction

class GetMessageActionImpl(pubnub: PubNub, params: PubNub.GetMessageActionsParameters) : GetMessageActions, EndpointImpl<PubNub.GetMessageActionsResponse, PNGetMessageActionsResult>(
    promiseFactory = { pubnub.getMessageActions(params) },
    responseMapping = {
        PNGetMessageActionsResult(
            it.data.map { action ->
                action.toMessageAction()
            },
            it.start?.let { startNotNull ->
                PNBoundedPage(startNotNull.toLong(), it.end?.toLong(), null) //TODO kmp does js not return limit?
            }
        )
    }
)