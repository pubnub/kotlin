package com.pubnub.api.models.server.access_manager

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeysData

class AccessManagerGrantPayload {

    internal lateinit var level: String

    internal var ttl = 0

    @SerializedName("subscribe_key")
    internal lateinit var subscribeKey: String

    internal val channels: Map<String, PNAccessManagerKeysData>? = null

    @SerializedName("channel-groups")
    internal val channelGroups: JsonElement? = null

    @SerializedName("auths")
    internal val authKeys: Map<String, PNAccessManagerKeyData>? = null

    internal val channel: String? = null
}