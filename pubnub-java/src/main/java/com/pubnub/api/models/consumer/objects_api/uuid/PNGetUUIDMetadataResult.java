package com.pubnub.api.models.consumer.objects_api.uuid;

import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNGetUUIDMetadataResult extends EntityEnvelope<PNUUIDMetadata> {
    public PNGetUUIDMetadataResult(final EntityEnvelope<PNUUIDMetadata> envelope) {
        this.status = envelope.getStatus();
        this.data = envelope.getData();
    }
}
