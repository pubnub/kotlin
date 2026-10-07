package com.pubnub.api.java.models.consumer.datasync.membership;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncCreateMembershipResult extends EntityEnvelope<PNDataSyncMembership> {
    public PNDataSyncCreateMembershipResult(int status, PNDataSyncMembership data) {
        this.status = status;
        this.data = data;
    }
}
