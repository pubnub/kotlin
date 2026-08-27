package com.pubnub.api.java.endpoints.datasync.channel;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.channel.DataSyncUpdateChannelResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#updateChannel(String, java.util.List)
 */
public interface UpdateChannel extends Endpoint<DataSyncUpdateChannelResult> {
    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    UpdateChannel ifMatch(@Nullable String ifMatch);
}