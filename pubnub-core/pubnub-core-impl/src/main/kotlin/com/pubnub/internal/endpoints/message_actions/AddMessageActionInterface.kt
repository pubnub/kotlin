package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction

interface AddMessageActionInterface : ExtendedRemoteAction<PNAddMessageActionResult> {
    val channel: String
    val messageAction: PNMessageAction
}
