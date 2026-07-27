package com.pubnub.api.java.models.consumer.datasync.entity;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNGetEntityResult extends EntityEnvelope<PNEntity> {
    public PNGetEntityResult(int status, PNEntity data) {
        this.status = status;
        this.data = data;
    }
}
