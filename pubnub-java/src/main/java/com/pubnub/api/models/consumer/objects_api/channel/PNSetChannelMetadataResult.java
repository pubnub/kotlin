package com.pubnub.api.models.consumer.objects_api.channel;

import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNSetChannelMetadataResult extends EntityEnvelope<PNChannelMetadata> {
    public PNSetChannelMetadataResult(final EntityEnvelope<PNChannelMetadata> envelope) {
        this.status = envelope.getStatus();
        this.data = envelope.getData();
    }
}
