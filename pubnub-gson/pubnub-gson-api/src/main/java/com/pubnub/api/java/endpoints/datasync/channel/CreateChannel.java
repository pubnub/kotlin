package com.pubnub.api.java.endpoints.datasync.channel;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncCreateChannelResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#createChannel(int)
 */
public interface CreateChannel extends Endpoint<PNDataSyncCreateChannelResult> {
    /**
     * Optional channel identifier. When not set the server generates one.
     */
    CreateChannel channelId(@Nullable String channelId);

    /**
     * Optional entity class identifier. When not set the server defaults it to {@code Channel}.
     * When set it must be a {@code Channel} subclass.
     */
    CreateChannel className(@Nullable String className);

    /**
     * Optional level at which the entity class is defined. Disambiguates a class defined at more than
     * one level. Create-only — not accepted by {@code setChannel}. The built-in {@code Channel} class is
     * defined at the {@code GLOBAL} level.
     */
    CreateChannel classLevel(@Nullable PNDataSyncClassLevel classLevel);

    /**
     * Optional channel status.
     */
    CreateChannel status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    CreateChannel payload(@Nullable Map<String, Object> payload);
}
