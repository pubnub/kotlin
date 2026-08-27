package com.pubnub.api.java.models.consumer.datasync.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class DataSyncRemoveChannelResult {
    private int status;

    public DataSyncRemoveChannelResult(int status) {
        this.status = status;
    }
}
