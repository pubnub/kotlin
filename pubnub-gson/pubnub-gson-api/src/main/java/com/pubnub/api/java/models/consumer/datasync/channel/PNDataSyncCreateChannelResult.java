package com.pubnub.api.java.models.consumer.datasync.channel;

import com.pubnub.api.java.models.consumer.objects_api.EntityEnvelope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PNDataSyncCreateChannelResult extends EntityEnvelope<PNDataSyncChannel> {
    public PNDataSyncCreateChannelResult(int status, PNDataSyncChannel data) {
        this.status = status;
        this.data = data;
    }
}
