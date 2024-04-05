package com.pubnub.api.v2.endpoints.pubsub;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPublishResult;

public interface PublishBuilder extends Endpoint<PNPublishResult> {
    PublishBuilder shouldStore(Boolean shouldStore);

    PublishBuilder usePOST(boolean usePOST);

    PublishBuilder meta(Object meta);

    PublishBuilder replicate(boolean replicate);

    PublishBuilder ttl(Integer ttl);
}
