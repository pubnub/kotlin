package com.pubnub.api.models.consumer.objects_api.uuid;

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult;
import lombok.ToString;

@ToString(callSuper = true)
public class PNUUIDMetadataResult extends ObjectResult<PNUUIDMetadata> {
    public PNUUIDMetadataResult(BasePubSubResult result, String event, PNUUIDMetadata data) {
        super(result, event, data);
    }
}
