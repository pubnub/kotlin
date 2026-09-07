package com.pubnub.api.java.models.consumer.datasync.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * DataSync Channel resource returned by {@code pubnub.dataSync().getChannel()} / {@code createChannel()}.
 *
 * <p>A Channel is a specialized DataSync entity of class {@code Channel}.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class PNDataSyncChannel {
    /**
     * Channel identifier.
     */
    private String id;

    /**
     * Entity class identifier (a {@code Channel} subclass, defaults to {@code Channel}).
     */
    private String className;

    /**
     * Version of the entity class.
     */
    private int classVersion;

    /**
     * The level at which the entity class is defined (e.g. {@code SubKey} / {@code Global}).
     */
    @Nullable
    private String classLevel;

    /**
     * Date and time the channel was created.
     */
    private String createdAt;

    /**
     * Date and time the channel was last updated.
     */
    private String updatedAt;

    /**
     * The channel's content fingerprint used in conditional requests.
     */
    private String eTag;

    /**
     * Channel status.
     */
    @Nullable
    private String status;

    /**
     * Date and time when the channel expires (will be deleted automatically). Always present —
     * server-computed from the entity class TTL and never client-settable.
     */
    private String expiresAt;

    /**
     * Arbitrary user-defined JSON object.
     */
    @Nullable
    private Map<String, Object> payload;
}