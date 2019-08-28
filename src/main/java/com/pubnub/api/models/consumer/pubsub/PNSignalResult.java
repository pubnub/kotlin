package com.pubnub.api.models.consumer.pubsub;

import com.google.gson.JsonElement;

import lombok.ToString;

@ToString(callSuper = true)
public class PNSignalResult extends MessageResult {

    public PNSignalResult(BasePubSubResult basePubSubResult, JsonElement message) {
        super(basePubSubResult, message);
    }
}