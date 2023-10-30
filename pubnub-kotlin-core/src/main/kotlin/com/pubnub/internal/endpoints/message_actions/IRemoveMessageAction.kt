package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult

interface IRemoveMessageAction : ExtendedRemoteAction<PNRemoveMessageActionResult> {
    val channel: String
    val messageTimetoken: Long
    val actionTimetoken: Long
}