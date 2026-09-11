package com.pubnub.api.java.models.consumer.datasync.relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncRemoveRelationshipResult {
    private int status;

    public PNDataSyncRemoveRelationshipResult(int status) {
        this.status = status;
    }
}
