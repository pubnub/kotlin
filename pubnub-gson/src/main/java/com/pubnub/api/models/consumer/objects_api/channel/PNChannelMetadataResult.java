package com.pubnub.api.models.consumer.objects_api.channel;

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult;
import lombok.ToString;

@ToString(callSuper = true)
public class PNChannelMetadataResult extends ObjectResult<PNChannelMetadata> {
    public  PNChannelMetadataResult(BasePubSubResult result, String event, PNChannelMetadata data) {
        super(result, event, data);
    }
}
