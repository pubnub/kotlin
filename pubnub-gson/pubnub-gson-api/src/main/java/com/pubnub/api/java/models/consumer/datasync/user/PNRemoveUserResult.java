package com.pubnub.api.java.models.consumer.datasync.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNRemoveUserResult {
    private int status;

    public PNRemoveUserResult(int status) {
        this.status = status;
    }
}
