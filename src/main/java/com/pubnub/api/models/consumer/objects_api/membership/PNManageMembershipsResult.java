package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Getter;

@Getter
public class PNManageMembershipsResult extends EntityArrayEnvelope<PNMembership> {

    public static PNManageMembershipsResult create(EntityArrayEnvelope<PNMembership> envelope) {
        PNManageMembershipsResult result = new PNManageMembershipsResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNManageMembershipsResult create() {
        return new PNManageMembershipsResult();
    }
}
