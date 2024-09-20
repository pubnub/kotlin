package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNSetChannelMetadataResult extends EntityEnvelope<PNChannelMetadata> {
    public PNSetChannelMetadataResult(int status, PNChannelMetadata data) {
        this.status = status;
        this.data = data;
    }
}
