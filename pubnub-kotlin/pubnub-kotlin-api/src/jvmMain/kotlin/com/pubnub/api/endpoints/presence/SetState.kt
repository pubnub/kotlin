package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNSetStateResult

/**
 * @see [PubNub.setPresenceState]
 */
interface SetState : com.pubnub.kmp.endpoints.presence.SetState, Endpoint<PNSetStateResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val state: Any
    val uuid: String
}
