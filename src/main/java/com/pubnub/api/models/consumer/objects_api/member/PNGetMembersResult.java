package com.pubnub.api.models.consumer.objects_api.member;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Getter;

@Getter
public class PNGetMembersResult extends EntityArrayEnvelope<PNMember> {

    public static PNGetMembersResult create(EntityArrayEnvelope<PNMember> envelope) {
        PNGetMembersResult result = new PNGetMembersResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNGetMembersResult create() {
        return new PNGetMembersResult();
    }

}
