package com.pubnub.api.models.consumer.objects_api.uuid;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNGetAllUUIDMetadataResult extends EntityArrayEnvelope<PNUUIDMetadata> {

    public PNGetAllUUIDMetadataResult(EntityArrayEnvelope<PNUUIDMetadata> envelope) {
        this.status = envelope.getStatus();
        this.totalCount = envelope.getTotalCount();
        this.prev = envelope.getPrev();
        this.next = envelope.getNext();
        this.data = envelope.getData();
    }
}
