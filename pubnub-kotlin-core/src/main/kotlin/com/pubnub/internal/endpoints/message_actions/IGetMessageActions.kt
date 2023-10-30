package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult

interface IGetMessageActions : ExtendedRemoteAction<PNGetMessageActionsResult> {
    val channel: String
    val page: PNBoundedPage
}