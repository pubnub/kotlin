package com.pubnub.api.java.models.consumer.datasync.entity;

import org.jetbrains.annotations.NotNull;

public interface DataSyncEntityConverter {
    @NotNull
    static DataSyncEntity from(@NotNull com.pubnub.api.models.consumer.datasync.entity.DataSyncEntity data) {
        return new DataSyncEntity()
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
