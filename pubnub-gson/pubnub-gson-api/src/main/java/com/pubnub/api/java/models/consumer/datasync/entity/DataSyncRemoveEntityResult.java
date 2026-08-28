package com.pubnub.api.java.models.consumer.datasync.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class DataSyncRemoveEntityResult {
    private int status;

    public DataSyncRemoveEntityResult(int status) {
        this.status = status;
    }
}
