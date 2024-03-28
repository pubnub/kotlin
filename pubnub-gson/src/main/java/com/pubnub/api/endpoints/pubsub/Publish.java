package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPublishResult;

public interface Publish extends Endpoint<PNPublishResult> {
    Publish message(Object message);

    Publish channel(String channel);

    Publish shouldStore(Boolean shouldStore);

    Publish usePOST(boolean usePOST);

    Publish meta(Object meta);

    Publish replicate(boolean replicate);

    Publish ttl(Integer ttl);
}
