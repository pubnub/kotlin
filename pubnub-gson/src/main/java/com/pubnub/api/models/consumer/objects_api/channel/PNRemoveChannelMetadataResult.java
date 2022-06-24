package com.pubnub.api.models.consumer.objects_api.channel;

import com.google.gson.JsonElement;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PACKAGE)
@ToString(callSuper = true)
public class PNRemoveChannelMetadataResult extends EntityEnvelope<JsonElement> {
    public PNRemoveChannelMetadataResult(final EntityEnvelope<JsonElement> envelope) {
        this.status = envelope.getStatus();
        this.data = envelope.getData();
    }
}
