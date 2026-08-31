package com.pubnub.api.java.models.consumer.datasync.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncRemoveUserResult {
    private int status;

    public PNDataSyncRemoveUserResult(int status) {
        this.status = status;
    }
}
