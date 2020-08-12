package com.pubnub.api.models.consumer.objects.channel

import java.time.Instant

data class PNChannelMetadata(
    val id: String,
    val name: String?,
    val description: String?,
    val custom: Any?,
    val updated: Instant,
    val eTag: String
)
