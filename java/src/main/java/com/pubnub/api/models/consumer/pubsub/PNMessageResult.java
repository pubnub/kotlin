package com.pubnub.api.models.consumer.pubsub;

import com.google.gson.JsonElement;

import lombok.ToString;

@ToString(callSuper = true)
public class PNMessageResult extends MessageResult {

    public PNMessageResult(BasePubSubResult basePubSubResult, JsonElement message) {
        super(basePubSubResult, message);
    }
}

