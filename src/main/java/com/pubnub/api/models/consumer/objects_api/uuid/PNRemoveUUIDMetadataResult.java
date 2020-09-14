package com.pubnub.api.models.consumer.objects_api.uuid;

import com.google.gson.JsonElement;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@ToString(callSuper = true)
public class PNRemoveUUIDMetadataResult extends EntityEnvelope<JsonElement> {
    public PNRemoveUUIDMetadataResult(final EntityEnvelope<JsonElement> envelope) {
        this.status = envelope.getStatus();
        this.data = envelope.getData();
    }
}
