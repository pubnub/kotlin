package com.pubnub.api.java.endpoints.datasync.channel;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.channel.DataSyncSetChannelResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @see com.pubnub.api.java.datasync.DataSync#setChannel(String, int)
 */
public interface SetChannel extends Endpoint<DataSyncSetChannelResult> {
    /**
     * Optional channel status.
     */
    SetChannel status(@Nullable String status);

    /**
     * Optional arbitrary JSON object payload.
     */
    SetChannel payload(@Nullable Map<String, Object> payload);

    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    SetChannel ifMatch(@Nullable String ifMatch);
}
