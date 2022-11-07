package com.pubnub.api.models.server.objects_api

internal data class MembershipInput(
    val channel: String,
    val custom: Any? = null,
    val status: String? = null
)
