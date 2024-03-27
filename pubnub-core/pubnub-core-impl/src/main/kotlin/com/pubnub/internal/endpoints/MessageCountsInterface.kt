package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.history.PNMessageCountResult

interface MessageCountsInterface : ExtendedRemoteAction<PNMessageCountResult> {
    val channels: List<String>
    val channelsTimetoken: List<Long>
}
