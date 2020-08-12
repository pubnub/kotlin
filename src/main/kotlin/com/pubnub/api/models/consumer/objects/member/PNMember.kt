package com.pubnub.api.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import java.time.Instant

data class PNMember(
    val uuid: PNUUIDMetadata?,
    val custom: Any? = null,
    val updated: Instant,
    val eTag: String
)
