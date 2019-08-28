package com.pubnub.api.models.consumer.pubsub;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class MessageResult extends BasePubSubResult {

    private JsonElement message;

    public MessageResult(BasePubSubResult basePubSubResult, JsonElement message) {
        super(basePubSubResult);
        this.message = message;
    }
}

