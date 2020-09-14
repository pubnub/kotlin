package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class PNGetMembershipsResult extends EntityArrayEnvelope<PNMembership> {
    public PNGetMembershipsResult(EntityArrayEnvelope<PNMembership> body) {
        this.data = body.getData();
        this.next = body.getNext();
        this.prev = body.getPrev();
        this.status = body.getStatus();
        this.totalCount = body.getTotalCount();
    }
}
