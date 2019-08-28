package com.pubnub.api.models.consumer.pubsub.objects;

import com.google.gson.JsonElement;
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public class PNMembershipResult extends ObjectResult<JsonElement> {

    @Builder(builderMethodName = "membershipBuilder")
    private PNMembershipResult(BasePubSubResult result, String event, JsonElement data) {
        super(result, event, data);
    }

    public JsonElement getData() {
        return data;
    }
}

