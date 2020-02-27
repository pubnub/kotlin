package com.pubnub.api.models.consumer.objects_api.util;

import com.pubnub.api.endpoints.Endpoint;

public interface FilteringParamsProvider<PubNubEndpoint extends Endpoint> {

    PubNubEndpoint filter(String expression);
}
