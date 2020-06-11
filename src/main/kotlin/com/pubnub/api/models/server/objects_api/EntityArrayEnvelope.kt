package com.pubnub.api.models.server.objects_api

open class EntityArrayEnvelope<T> : EntityEnvelope<List<T>>() {

    var totalCount: Int = 0
    var next: String? = null
    var prev: String? = null

}