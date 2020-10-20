package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata

data class PNChannelMembership(
    val channel: PNChannelMetadata?,
    val custom: Any?,
    val updated: String,
    val eTag: String
)
