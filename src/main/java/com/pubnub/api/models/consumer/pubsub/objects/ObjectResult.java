package com.pubnub.api.models.consumer.pubsub.objects;

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
public class ObjectResult<T> extends BasePubSubResult {

    @Getter
    protected String event;
    protected T data;

    protected ObjectResult(BasePubSubResult result, String event, T data) {
        super(result);
        this.event = event;
        this.data = data;
    }
}