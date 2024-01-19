package com.pubnub.api.models.consumer.objects_api.uuid;

import com.google.gson.JsonElement;
import com.pubnub.api.models.consumer.objects_api.EntityEnvelope;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PACKAGE)
@ToString(callSuper = true)
public class PNRemoveUUIDMetadataResult extends EntityEnvelope<JsonElement> {
    public PNRemoveUUIDMetadataResult(final EntityEnvelope<JsonElement> envelope) {
        this.status = envelope.getStatus();
        this.data = envelope.getData();
    }

    public PNRemoveUUIDMetadataResult(int status, JsonElement data) {
        this.status = status;
        this.data = data;
    }
}
