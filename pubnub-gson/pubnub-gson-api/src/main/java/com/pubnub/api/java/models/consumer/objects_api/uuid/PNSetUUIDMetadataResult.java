package com.pubnub.api.java.models.consumer.objects_api.uuid;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNSetUUIDMetadataResult extends EntityEnvelope<PNUUIDMetadata> {
    public PNSetUUIDMetadataResult(final EntityEnvelope<PNUUIDMetadata> envelope) {
        this.status = envelope.getStatus();
        this.data = envelope.getData();
    }

    public PNSetUUIDMetadataResult(int status, PNUUIDMetadata data) {
        this.status = status;
        this.data = data;
    }
}
