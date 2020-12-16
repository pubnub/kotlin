package com.pubnub.api.models.consumer.objects.membership

data class PNChannelWithCustom(
    val channel: String,
    val custom: Any? = null
) {
    companion object {
        @Deprecated(
            message = "Use constructor instead",
            level = DeprecationLevel.WARNING,
            replaceWith = ReplaceWith(
                "PNChannelWithCustom(channel = channel, custom = custom)",
                "com.pubnub.api.models.consumer.objects.membership.PNChannelWithCustom"
            )
        )
        fun of(
            channel: String,
            custom: Any? = null
        ) = PNChannelWithCustom(channel = channel, custom = custom)
    }
}
