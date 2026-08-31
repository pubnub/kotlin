package com.pubnub.api.java.endpoints.datasync.channel;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncRemoveChannelResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#removeChannel(String)
 */
public interface RemoveChannel extends Endpoint<PNDataSyncRemoveChannelResult> {
    /**
     * Optional eTag for a conditional delete ({@code If-Match} header).
     */
    RemoveChannel ifMatch(@Nullable String ifMatch);
}