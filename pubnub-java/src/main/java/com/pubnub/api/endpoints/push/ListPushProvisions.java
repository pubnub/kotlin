package com.pubnub.api.endpoints.push;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;

public interface ListPushProvisions extends Endpoint<PNPushListProvisionsResult> {
    ListPushProvisions pushType(com.pubnub.api.enums.PNPushType pushType);

    ListPushProvisions deviceId(String deviceId);

    ListPushProvisions environment(com.pubnub.api.enums.PNPushEnvironment environment);

    ListPushProvisions topic(String topic);
}
