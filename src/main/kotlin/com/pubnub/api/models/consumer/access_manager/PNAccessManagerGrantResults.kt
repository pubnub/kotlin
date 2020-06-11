package com.pubnub.api.models.consumer.access_manager

import com.google.gson.annotations.SerializedName

class PNAccessManagerGrantResult(
    val level: String,
    val ttl: Int,
    val subscribeKey: String,
    val channels: Map<String, Map<String, PNAccessManagerKeyData>?>,
    val channelGroups: Map<String, Map<String, PNAccessManagerKeyData>?>
)

class PNAccessManagerKeyData {

    @SerializedName("r")
    internal var readEnabled: Boolean = false

    @SerializedName("w")
    internal var writeEnabled: Boolean = false

    @SerializedName("m")
    internal var manageEnabled: Boolean = false

    @SerializedName("d")
    internal var deleteEnabled: Boolean = false
}

class PNAccessManagerKeysData {
    @SerializedName("auths")
    internal val authKeys: Map<String, PNAccessManagerKeyData>? = null
}