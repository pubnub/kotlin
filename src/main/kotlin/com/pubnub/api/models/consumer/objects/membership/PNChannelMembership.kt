package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import java.time.Instant

data class PNChannelMembership(
    val channel: PNChannelMetadata?,
    val custom: Any?,
    val updated: Instant,
    val eTag: String
)
