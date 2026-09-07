package com.pubnub.api.java.models.consumer.datasync.membership;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncRemoveMembershipResult {
    private int status;

    public PNDataSyncRemoveMembershipResult(int status) {
        this.status = status;
    }
}
