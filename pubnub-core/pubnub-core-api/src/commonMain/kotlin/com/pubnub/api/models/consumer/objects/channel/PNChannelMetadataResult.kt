package com.pubnub.api.models.consumer.objects.channel

data class PNChannelMetadataResult(
    @Deprecated("This field's value is always `200` and so it will be removed in a future release.")
    val status: Int,
    val data: PNChannelMetadata,
)
