package com.pubnub.api.models.consumer.access_manager

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
)

open class PNAccessManagerKeyData {
    /**
     * Is `true` if *read* rights are granted.
     */
    var readEnabled: Boolean = false

    /**
     * Is `true` if *write* rights are granted.
     */
    var writeEnabled: Boolean = false

    /**
     * Is `true` if *manage* rights are granted.
     */
    var manageEnabled: Boolean = false

    /**
     * Is `true` if *delete* rights are granted.
     */
    var deleteEnabled: Boolean = false
    var getEnabled: Boolean = false
    var updateEnabled: Boolean = false
    var joinEnabled: Boolean = false
}

class PNAccessManagerKeysData {
    val authKeys: Map<String, PNAccessManagerKeyData>? = null
}
