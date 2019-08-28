package com.pubnub.api.models.consumer.pubsub.objects;


import com.pubnub.api.models.consumer.objects_api.space.PNSpace;
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public class PNSpaceResult extends ObjectResult<PNSpace> {

    @Builder(builderMethodName = "spaceBuilder")
    private PNSpaceResult(BasePubSubResult result, String event, PNSpace space) {
        super(result, event, space);
    }

    public PNSpace getSpace() {
        return data;
    }
}

