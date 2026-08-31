package com.pubnub.api.java.models.consumer.datasync.channel;

import org.jetbrains.annotations.NotNull;

public interface PNDataSyncChannelConverter {
    @NotNull
    static PNDataSyncChannel from(@NotNull com.pubnub.api.models.consumer.datasync.channel.PNDataSyncChannel data) {
        return new PNDataSyncChannel()
                .setId(data.getId())
                .setClassName(data.getClassName())
                .setClassVersion(data.getClassVersion())
                .setClassLevel(data.getClassLevel())
                .setCreatedAt(data.getCreatedAt())
                .setUpdatedAt(data.getUpdatedAt())
                .setETag(data.getETag())
                .setStatus(data.getStatus())
                .setExpiresAt(data.getExpiresAt())
                .setPayload(data.getPayload());
    }
}
