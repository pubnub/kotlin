package com.pubnub.api.models.consumer.objects.membership

/**
 * Factory function to create an instance of [MembershipInclude].
 *
 * This function provides default values for all inclusion flags and returns an
 * implementation of the [MembershipInclude] interface with the specified options.
 *
 * @param includeCustom Whether to include custom properties in the result. Default is `false`.
 * @param includeStatus Whether to include the status of the Memberships in the result. Default is `false`.
 * @param includeType Whether to include the type of the Memberships in the result. Default is `false`.
 * @param includeTotalCount Whether to include the total count of Memberships in the result. Default is `false`.
 * @param includeChannel Whether to include channel information in the result. Default is `false`.
 * @param includeChannelCustom Whether to include custom properties of the channel in the result. Default is `false`.
 * @param includeChannelType Whether to include the type of the channel in the result. Default is `false`.
 * @param includeChannelStatus Whether to include the status of the channel in the result. Default is `false`.
 *
 * @return An instance of [MembershipInclude] with the specified inclusion options.
 */
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
