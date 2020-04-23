package com.pubnub.api.models.consumer.objects_api.util

interface ListingParamsProvider {

    var limit: Int?
    var start: String?
    var end: String?
    var withTotalCount: Boolean?
}