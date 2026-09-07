package com.pubnub.api.java.models.consumer.datasync.relationship;

import org.jetbrains.annotations.NotNull;

public interface PNDataSyncRelationshipConverter {
    @NotNull
    static PNDataSyncRelationship from(
            @NotNull com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship data) {
        return new PNDataSyncRelationship()
                .setId(data.getId())
                .setEntityAId(data.getEntityAId())
                .setEntityBId(data.getEntityBId())
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
