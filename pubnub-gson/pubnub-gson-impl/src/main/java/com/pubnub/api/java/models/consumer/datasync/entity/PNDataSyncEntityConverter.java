package com.pubnub.api.java.models.consumer.datasync.entity;

import org.jetbrains.annotations.NotNull;

public interface PNDataSyncEntityConverter {
    @NotNull
    static PNDataSyncEntity from(@NotNull com.pubnub.api.models.consumer.datasync.entity.PNDataSyncEntity data) {
        return new PNDataSyncEntity()
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
