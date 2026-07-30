package com.pubnub.api.java.models.consumer.datasync.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNRemoveEntityResult {
    private int status;

    public PNRemoveEntityResult(int status) {
        this.status = status;
    }
}
