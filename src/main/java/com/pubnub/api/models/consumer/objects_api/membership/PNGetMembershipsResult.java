package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Getter;

@Getter
public class PNGetMembershipsResult extends EntityArrayEnvelope<PNMembership> {

    public static PNGetMembershipsResult create(EntityArrayEnvelope<PNMembership> envelope) {
        PNGetMembershipsResult result = new PNGetMembershipsResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNGetMembershipsResult create() {
        return new PNGetMembershipsResult();
    }
}
