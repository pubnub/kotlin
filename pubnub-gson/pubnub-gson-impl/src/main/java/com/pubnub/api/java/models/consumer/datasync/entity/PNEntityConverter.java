package com.pubnub.api.java.models.consumer.datasync.entity;

import org.jetbrains.annotations.NotNull;

public interface PNEntityConverter {
    @NotNull
    static PNEntity from(@NotNull com.pubnub.api.models.consumer.datasync.entity.PNEntity data) {
        return new PNEntity()
                .setId(data.getId())
                .setEntityClass(data.getEntityClass())
                .setEntityClassVersion(data.getEntityClassVersion())
                .setEntityClassLevel(data.getEntityClassLevel())
                .setCreatedAt(data.getCreatedAt())
                .setUpdatedAt(data.getUpdatedAt())
                .setETag(data.getETag())
                .setStatus(data.getStatus())
                .setExpiresAt(data.getExpiresAt())
                .setPayload(data.getPayload());
    }
}
