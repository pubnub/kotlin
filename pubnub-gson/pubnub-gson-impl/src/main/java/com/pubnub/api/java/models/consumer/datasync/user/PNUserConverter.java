package com.pubnub.api.java.models.consumer.datasync.user;

import org.jetbrains.annotations.NotNull;

public interface PNUserConverter {
    @NotNull
    static PNUser from(@NotNull com.pubnub.api.models.consumer.datasync.user.PNUser data) {
        return new PNUser()
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
