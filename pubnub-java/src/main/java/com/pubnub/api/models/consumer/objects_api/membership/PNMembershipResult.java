package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult;
import lombok.ToString;

@ToString(callSuper = true)
public class PNMembershipResult extends ObjectResult<PNMembership> {
    public PNMembershipResult(BasePubSubResult result, String event, PNMembership data) {
        super(result, event, data);
    }
}
