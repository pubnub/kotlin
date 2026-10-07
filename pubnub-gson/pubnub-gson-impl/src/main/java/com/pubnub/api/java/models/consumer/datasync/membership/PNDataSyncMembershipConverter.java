package com.pubnub.api.java.models.consumer.datasync.membership;

import org.jetbrains.annotations.NotNull;

public interface PNDataSyncMembershipConverter {
    @NotNull
    static PNDataSyncMembership from(
            @NotNull com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership data) {
        return new PNDataSyncMembership()
                .setId(data.getId())
                .setChannelId(data.getChannelId())
                .setUserId(data.getUserId())
                .setClassName(data.getClassName())
                .setClassVersion(data.getClassVersion())
                .setCreatedAt(data.getCreatedAt())
                .setUpdatedAt(data.getUpdatedAt())
                .setETag(data.getETag())
                .setStatus(data.getStatus())
                .setExpiresAt(data.getExpiresAt())
                .setPayload(data.getPayload());
    }
}