package com.pubnub.internal.models.consumer.access_manager

import com.google.gson.annotations.SerializedName
import com.pubnub.internal.BasePubNub.PubNubImpl

/**
 * Result of the [PubNubImpl.grant] operation
 *
 * @property level Permissions level, one of `subkey`, `subkey+auth`, `channel`, `channel-group`,
 * `channel-group+auth` level.
 * @property ttl Time in minutes for which granted permissions are valid. Value of `0` means indefinite.
 * @property subscribeKey The subscribe key.
 * @property channels Access rights per channel.
 * @property channelGroups Access rights per channel group.
 */
class PNAccessManagerGrantResult(
    val level: String,
    val ttl: Int,
    val subscribeKey: String,
    val channels: Map<String, Map<String, PNAccessManagerKeyData>?>,
    val channelGroups: Map<String, Map<String, PNAccessManagerKeyData>?>
)

open class PNAccessManagerKeyData {

    /**
     * Is `true` if *read* rights are granted.
     */
    @SerializedName("r")
    var readEnabled: Boolean = false

    /**
     * Is `true` if *write* rights are granted.
     */
    @SerializedName("w")
    var writeEnabled: Boolean = false

    /**
     * Is `true` if *manage* rights are granted.
     */
    @SerializedName("m")
    var manageEnabled: Boolean = false

    /**
     * Is `true` if *delete* rights are granted.
     */
    @SerializedName("d")
    var deleteEnabled: Boolean = false

    @SerializedName("g")
    var getEnabled: Boolean = false

    @SerializedName("u")
    var updateEnabled: Boolean = false

    @SerializedName("j")
    var joinEnabled: Boolean = false
}

class PNAccessManagerKeysData {
    @SerializedName("auths")
    val authKeys: Map<String, PNAccessManagerKeyData>? = null
}
