package com.pubnub.api.models.consumer.objects.membership

data class PNChannelWithCustom internal constructor(
    val channel: String,
    val custom: Any? = null
) {
    companion object {
        fun of(
            channel: String,
            custom: Any? = null
        ) = PNChannelWithCustom(channel = channel, custom = custom)
    }
}
