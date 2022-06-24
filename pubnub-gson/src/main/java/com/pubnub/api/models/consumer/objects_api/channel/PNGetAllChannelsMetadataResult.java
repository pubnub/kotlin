package com.pubnub.api.models.consumer.objects_api.channel;

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNGetAllChannelsMetadataResult extends EntityArrayEnvelope<PNChannelMetadata> {

    public PNGetAllChannelsMetadataResult(EntityArrayEnvelope<PNChannelMetadata> envelope) {
        this.status = envelope.getStatus();
        this.totalCount = envelope.getTotalCount();
        this.prev = envelope.getPrev();
        this.next = envelope.getNext();
        this.data = envelope.getData();
    }
}
