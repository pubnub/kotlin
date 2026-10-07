package com.pubnub.api.java.models.consumer.datasync.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncRemoveEntityResult {
    private int status;

    public PNDataSyncRemoveEntityResult(int status) {
        this.status = status;
    }
}
