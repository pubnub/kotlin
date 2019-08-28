package com.pubnub.api.models.consumer.objects_api.util;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.PropertyEnvelope;

public interface MembershipChainProvider<PubNubEndpoint extends Endpoint, PropertyType extends PropertyEnvelope> {

    PubNubEndpoint add(PropertyType... list);

    PubNubEndpoint update(PropertyType... list);

    PubNubEndpoint remove(PropertyType... list);
}
