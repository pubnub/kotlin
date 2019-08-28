package com.pubnub.api.models.consumer.objects_api.space;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;

import lombok.Getter;

@Getter
public class PNGetSpacesResult extends EntityArrayEnvelope<PNSpace> {

    public static PNGetSpacesResult create(EntityArrayEnvelope<PNSpace> envelope) {
        PNGetSpacesResult result = new PNGetSpacesResult();
        result.totalCount = envelope.getTotalCount();
        result.next = envelope.getNext();
        result.prev = envelope.getPrev();
        result.data = envelope.getData();
        return result;
    }

    public static PNGetSpacesResult create() {
        return new PNGetSpacesResult();
    }
}
