package com.pubnub.api.java.models.consumer.objects_api.channel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PNChannelMetadataConverter {
    @NotNull
    static PNChannelMetadata from(@NotNull com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata data) {
        PNChannelMetadata newData = new PNChannelMetadata(
                data.getId(),
                data.getName(),
                data.getDescription()
        );
        newData.setETag(data.getETag());
        newData.setType(data.getType());
        newData.setStatus(data.getStatus());
        newData.setCustom(data.getCustom());
        newData.setUpdated(data.getUpdated());
        return newData;
    }

    static List<PNChannelMetadata> from(Collection<com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata> data) {
        List<PNChannelMetadata> channels = new ArrayList<>(data.size());
        for (com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata datum : data) {
            channels.add(from(datum));
        }
        return channels;
    }
}
