package com.pubnub.api.models.consumer.objects_api.util;

import com.pubnub.api.endpoints.Endpoint;

public interface ListingParamsProvider<PubNubEndpoint extends Endpoint> {

    PubNubEndpoint limit(Integer limit);

    PubNubEndpoint start(String start);

    PubNubEndpoint end(String end);

    PubNubEndpoint withTotalCount(Boolean count);
}
