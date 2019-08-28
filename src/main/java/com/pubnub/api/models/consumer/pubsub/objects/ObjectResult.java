package com.pubnub.api.models.consumer.pubsub.objects;

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
class ObjectResult<T> extends BasePubSubResult {

    @Getter
    protected String event;
    protected T data;

    ObjectResult(BasePubSubResult result, String event, T data) {
        super(result);
        this.event = event;
        this.data = data;
    }
}