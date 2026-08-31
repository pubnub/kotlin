package com.pubnub.api.java.models.consumer.datasync.entity;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncCreateEntityResult extends EntityEnvelope<PNDataSyncEntity> {
    public PNDataSyncCreateEntityResult(int status, PNDataSyncEntity data) {
        this.status = status;
        this.data = data;
    }
}
