package com.pubnub.api.java.models.consumer.datasync.user;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class DataSyncGetUserResult extends EntityEnvelope<DataSyncUser> {
    public DataSyncGetUserResult(int status, DataSyncUser data) {
        this.status = status;
        this.data = data;
    }
}
