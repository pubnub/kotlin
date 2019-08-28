package com.pubnub.api.models.consumer.objects_api.member;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Getter;

@Getter
public class PNManageMembersResult extends EntityArrayEnvelope<PNMember> {

    public static PNManageMembersResult create(EntityArrayEnvelope<PNMember> envelope) {
        PNManageMembersResult result = new PNManageMembersResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNManageMembersResult create() {
        return new PNManageMembersResult();
    }

}
