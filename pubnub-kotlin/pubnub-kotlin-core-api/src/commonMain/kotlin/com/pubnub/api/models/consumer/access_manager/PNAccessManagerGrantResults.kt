package com.pubnub.api.models.consumer.access_manager

import com.pubnub.api.utils.SerializedName

/**
 * Result of the [PubNubCore.grant] operation
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
    val channelGroups: Map<String, Map<String, PNAccessManagerKeyData>?>,
    val uuids: Map<String, Map<String, PNAccessManagerKeyData>?>,
)

open class PNAccessManagerKeyData {
    /**
     * Is `true` if *read* rights are granted.
     */
    @field:SerializedName("r")
    var readEnabled: Boolean = false

    /**
     * Is `true` if *write* rights are granted.
     */
    @field:SerializedName("w")
    var writeEnabled: Boolean = false

    /**
     * Is `true` if *manage* rights are granted.
     */
    @field:SerializedName("m")
    var manageEnabled: Boolean = false

    /**
     * Is `true` if *delete* rights are granted.
     */
    @field:SerializedName("d")
    var deleteEnabled: Boolean = false

    @field:SerializedName("g")
    var getEnabled: Boolean = false

    @field:SerializedName("u")
    var updateEnabled: Boolean = false

    @field:SerializedName("j")
    var joinEnabled: Boolean = false
}

class PNAccessManagerKeysData {
    @field:SerializedName("auths")
    val authKeys: Map<String, PNAccessManagerKeyData>? = null
}
