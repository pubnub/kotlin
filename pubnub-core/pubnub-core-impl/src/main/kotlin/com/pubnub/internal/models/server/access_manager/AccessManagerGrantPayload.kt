package com.pubnub.internal.models.server.access_manager

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.pubnub.internal.models.consumer.access_manager.PNAccessManagerKeyData
import com.pubnub.internal.models.consumer.access_manager.PNAccessManagerKeysData

class AccessManagerGrantPayload {
    internal var level: String? = null

    internal var ttl = 0

    @SerializedName("subscribe_key")
    internal var subscribeKey: String? = null

    internal val channels: Map<String, PNAccessManagerKeysData>? = null

    @SerializedName("channel-groups")
    internal val channelGroups: JsonElement? = null

    @SerializedName("auths")
    internal val authKeys: Map<String, PNAccessManagerKeyData>? = null

    @SerializedName("uuids")
    internal val uuids: Map<String, PNAccessManagerKeysData>? = null

    internal val channel: String? = null
}
