package com.pubnub.api.models.consumer.objects.membership

fun MembershipInclude(
    includeCustom: Boolean = false,
    includeStatus: Boolean = false,
    includeType: Boolean = false,
    includeTotalCount: Boolean = false,
    includeChannel: Boolean = false,
    includeChannelCustom: Boolean = false,
    includeChannelType: Boolean = false,
    includeChannelStatus: Boolean = false,
) = object : MembershipInclude {
    override val includeCustom: Boolean = includeCustom
    override val includeStatus: Boolean = includeStatus
    override val includeType: Boolean = includeType
    override val includeTotalCount: Boolean = includeTotalCount
    override val includeChannel: Boolean = includeChannel
    override val includeChannelCustom: Boolean = includeChannelCustom
    override val includeChannelType: Boolean = includeChannelType
    override val includeChannelStatus: Boolean = includeChannelStatus
}
