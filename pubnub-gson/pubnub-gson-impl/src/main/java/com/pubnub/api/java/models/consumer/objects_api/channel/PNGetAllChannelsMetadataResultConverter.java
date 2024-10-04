package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult;
import org.jetbrains.annotations.Nullable;

public interface PNGetAllChannelsMetadataResultConverter {
    @Nullable
    static PNGetAllChannelsMetadataResult from(@Nullable PNChannelMetadataArrayResult result) {
        if (result == null) {
            return null;
        }
        return new PNGetAllChannelsMetadataResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNChannelMetadataConverter.from(result.getData())
        );
    }
}
