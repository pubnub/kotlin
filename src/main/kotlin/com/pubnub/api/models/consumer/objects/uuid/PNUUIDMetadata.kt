package com.pubnub.api.models.consumer.objects.uuid

import java.time.Instant

data class PNUUIDMetadata(
    val id: String,
    val name: String?,
    val externalId: String?,
    val profileUrl: String?,
    val email: String?,
    val custom: Any?,
    val updated: Instant?,
    val eTag: String?
)
