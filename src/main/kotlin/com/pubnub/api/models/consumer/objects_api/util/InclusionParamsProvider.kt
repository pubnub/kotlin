package com.pubnub.api.models.consumer.objects_api.util

import com.pubnub.api.Endpoint

interface InclusionParamsProvider<FieldsEnumType : Enum<*>> {

    var includeFields: List<FieldsEnumType>

}