package com.pubnub.api.models.server.objects_api

open class EntityEnvelope<T> {
    var status = 0
        private set
    var data: T? = null
        internal set
}