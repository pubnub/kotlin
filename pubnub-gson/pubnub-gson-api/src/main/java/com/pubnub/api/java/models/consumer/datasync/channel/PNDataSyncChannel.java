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
    private String id;
    private String className;
    private int classVersion;

    /**
     * The level at which the entity class is defined (e.g. {@code SubKey} / {@code Global}).
     */
    @Nullable
    private String classLevel;

    private String createdAt;
    private String updatedAt;
    private String eTag;

    @Nullable
    private String status;

    @Nullable
    private String expiresAt;

    @Nullable
    private Map<String, Object> payload;
}