package com.pubnub.api.models.consumer.objects_api.util;

import com.pubnub.api.endpoints.Endpoint;

public interface InclusionParamsProvider<PubNubEndpoint extends Endpoint, FieldsEnumType extends Enum> {

    PubNubEndpoint includeFields(FieldsEnumType... params);
}