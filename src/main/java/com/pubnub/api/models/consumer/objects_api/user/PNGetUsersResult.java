package com.pubnub.api.models.consumer.objects_api.user;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class PNGetUsersResult extends EntityArrayEnvelope<PNUser> {

    public static PNGetUsersResult create(EntityArrayEnvelope<PNUser> envelope) {
        PNGetUsersResult result = new PNGetUsersResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNGetUsersResult create() {
        return new PNGetUsersResult();
    }
}
