package com.pubnub.api.models.consumer.objects.membership

interface ChannelMembershipInput {
    val channel: String
    val custom: Any?
    val status: String?
}

@Deprecated(
    message = "Use PNChannelMembership.Partial",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        "PNChannelMembership.Partial(channelId = channel, custom = custom)",
        "com.pubnub.api.models.consumer.objects.membership.PNChannelMembership"
    )
)
data class PNChannelWithCustom(
    override val channel: String,
    override val custom: Any? = null
) : ChannelMembershipInput {
    override val status: String? = null

    companion object {
        @Deprecated(
            message = "Use PNChannelMembership.Partial",
            level = DeprecationLevel.WARNING,
            replaceWith = ReplaceWith(
                "PNChannelMembership.Partial(channelId = channel, custom = custom)",
                "com.pubnub.api.models.consumer.objects.membership.PNChannelMembership"
            )
        )
        fun of(
            channel: String,
            custom: Any? = null
        ) = PNChannelWithCustom(channel = channel, custom = custom)
    }
}
