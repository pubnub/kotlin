package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult

/**
 * @see [PubNub.grant]
 */
interface Grant : Endpoint<PNAccessManagerGrantResult> {
    val read: Boolean
    val write: Boolean
    val manage: Boolean
    val delete: Boolean
    val get: Boolean
    val update: Boolean
    val join: Boolean
    val ttl: Int
    val authKeys: List<String>
    val channels: List<String>
    val channelGroups: List<String>
    val uuids: List<String>
}
