package com.pubnub.api.models.consumer.pubsub.objects;

import com.pubnub.api.models.consumer.objects_api.user.PNUser;
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public class PNUserResult extends ObjectResult<PNUser> {

    @Builder(builderMethodName = "userBuilder")
    private PNUserResult(BasePubSubResult result, String event, PNUser user) {
        super(result, event, user);
    }

    public PNUser getUser() {
        return data;
    }
}

