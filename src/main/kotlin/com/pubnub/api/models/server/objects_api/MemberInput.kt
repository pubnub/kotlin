package com.pubnub.api.models.server.objects_api

internal data class MemberInput(
    val uuid: String,
    val custom: Any? = null,
    val status: String? = null
)
