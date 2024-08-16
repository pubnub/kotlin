package com.pubnub.api.java.endpoints.pubsub;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPublishResult;

public interface Signal extends Endpoint<PNPublishResult> {
    Signal message(Object message);

    Signal channel(String channel);
}
