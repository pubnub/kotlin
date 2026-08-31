package com.pubnub.api.java.models.consumer.datasync.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncRemoveChannelResult {
    private int status;

    public PNDataSyncRemoveChannelResult(int status) {
        this.status = status;
    }
}
