package com.pubnub.api.java.models.consumer.datasync;

/**
 * The level at which a DataSync entity class is defined. Used on the request side of DataSync APIs
 * (e.g. {@code createChannel} / {@code getChannels}); the enum's {@link #getValue()} is written to the
 * {@code entity_class_level} wire parameter.
 *
 * <p>The read side (e.g. {@code DataSyncChannel.getClassLevel()}) is modeled as a {@link String} rather
 * than this enum, so a future or unexpected server value never fails deserialization.
 */
public enum PNDataSyncClassLevel {
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
     * <p>⚠️ Not yet supported by the server — passing this currently results in a {@code 400} response.
     * Reserved for a planned future release; use {@link #GLOBAL} or {@link #SUBKEY} for now.
     */
    ACCOUNT("Account");

    private final String value;

    PNDataSyncClassLevel(String value) {
        this.value = value;
    }

    /**
     * The wire string sent to the server.
     */
    public String getValue() {
        return value;
    }
}