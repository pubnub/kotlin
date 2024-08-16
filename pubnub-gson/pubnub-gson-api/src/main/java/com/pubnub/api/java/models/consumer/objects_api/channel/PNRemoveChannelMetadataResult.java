package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.google.gson.JsonElement;
import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PACKAGE)
@ToString(callSuper = true)
public class PNRemoveChannelMetadataResult extends EntityEnvelope<JsonElement> {
    public PNRemoveChannelMetadataResult(int status, JsonElement data) {
        this.status = status;
        this.data = data;
    }
}
