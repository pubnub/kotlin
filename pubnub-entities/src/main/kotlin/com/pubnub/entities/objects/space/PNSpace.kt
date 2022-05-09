package com.pubnub.entities.objects.space

data class PNSpace(
    val id: String,
    val name: String?,
    val description: String?,
    val custom: Any?,
    val updated: String?,
    val eTag: String?
)
