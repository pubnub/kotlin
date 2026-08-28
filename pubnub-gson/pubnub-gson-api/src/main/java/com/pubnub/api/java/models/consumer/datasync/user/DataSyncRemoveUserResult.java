package com.pubnub.api.java.models.consumer.datasync.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class DataSyncRemoveUserResult {
    private int status;

    public DataSyncRemoveUserResult(int status) {
        this.status = status;
    }
}
