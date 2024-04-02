package com.pubnub.api.models.consumer.objects_api.channel;

import com.pubnub.api.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNGetChannelMetadataResult extends EntityEnvelope<PNChannelMetadata> {
    public PNGetChannelMetadataResult(int status, PNChannelMetadata data) {
        this.status = status;
        this.data = data;
    }
}
