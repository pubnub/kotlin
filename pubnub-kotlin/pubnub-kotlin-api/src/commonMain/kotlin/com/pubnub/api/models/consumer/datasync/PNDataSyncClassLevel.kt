package com.pubnub.api.models.consumer.datasync

/**
 * The level at which a DataSync entity class is defined. Used on the request side of DataSync APIs
 * (e.g. [com.pubnub.api.datasync.DataSync.createChannel] / [com.pubnub.api.datasync.DataSync.getChannels]);
 * the enum's [value] is written to the `entity_class_level` wire parameter.
 *
 * The read side (e.g. `DataSyncChannel.classLevel`) is modeled as a [String] rather than this enum, so a
 * future or unexpected server value never fails deserialization.
 *
 * @property value The wire string sent to the server.
 */
enum class PNDataSyncClassLevel(val value: String) {
    /**
     * Class defined at the global level (shared across all keysets).
     */
    GLOBAL("Global"),

    /**
     * Class defined at the subscribe-key level.
     */
    SUBKEY("SubKey"),

    /**
     * Class defined at the account level.
     *
     * ⚠️ Not yet supported by the server — passing this currently results in a `400` response.
     * Reserved for a planned future release; use [GLOBAL] or [SUBKEY] for now.
     */
    ACCOUNT("Account"),
}
