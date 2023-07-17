package com.pubnub.api.models.server.objects_api

data class EntityEnvelope<T>(
    val status: Int,
    val data: T? = null
)
