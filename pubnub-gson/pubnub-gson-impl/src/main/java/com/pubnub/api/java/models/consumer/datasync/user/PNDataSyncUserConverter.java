package com.pubnub.api.java.models.consumer.datasync.user;

import org.jetbrains.annotations.NotNull;

public interface PNDataSyncUserConverter {
    @NotNull
    static PNDataSyncUser from(@NotNull com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser data) {
        return new PNDataSyncUser()
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
