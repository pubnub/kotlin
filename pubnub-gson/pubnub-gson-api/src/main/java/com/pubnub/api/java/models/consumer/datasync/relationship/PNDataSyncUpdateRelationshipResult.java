package com.pubnub.api.java.models.consumer.datasync.relationship;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncUpdateRelationshipResult extends EntityEnvelope<PNDataSyncRelationship> {
    public PNDataSyncUpdateRelationshipResult(int status, PNDataSyncRelationship data) {
        this.status = status;
        this.data = data;
    }
}
