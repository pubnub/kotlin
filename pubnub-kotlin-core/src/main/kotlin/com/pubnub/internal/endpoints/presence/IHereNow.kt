package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.presence.PNHereNowResult

interface IHereNow : ExtendedRemoteAction<PNHereNowResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val includeState: Boolean
    val includeUUIDs: Boolean
}