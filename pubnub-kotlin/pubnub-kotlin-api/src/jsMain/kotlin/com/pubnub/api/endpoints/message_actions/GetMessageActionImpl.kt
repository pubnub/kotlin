package com.pubnub.api.endpoints.message_actions

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction

class GetMessageActionImpl(pubnub: PubNub, params: PubNub.GetMessageActionsParameters) : GetMessageActions, EndpointImpl<PubNub.GetMessageActionsResponse, PNGetMessageActionsResult>(
    promiseFactory = { pubnub.getMessageActions(params) },
    responseMapping = {
        PNGetMessageActionsResult(
            it.data.map {
                PNMessageAction(
                    it.type,
                    it.value,
                    it.messageTimetoken.toLong()
                ).apply {
                    actionTimetoken = it.actionTimetoken.toLong()
                }
            },
            it.start?.let { startNotNull ->
                PNBoundedPage(startNotNull.toLong(), it.end?.toLong(), null) //TODO kmp does js not return limit?
            }
        )
    }
)