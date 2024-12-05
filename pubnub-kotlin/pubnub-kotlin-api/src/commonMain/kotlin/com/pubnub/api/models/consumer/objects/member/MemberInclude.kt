package com.pubnub.api.models.consumer.objects.member

/**
 * Factory function to create an instance of [MemberInclude].
 *
 * This function provides default values for all inclusion flags and returns an
 * implementation of the [MemberInclude] interface with the specified options.
 *
 * @param includeCustom Whether to include custom properties in the result. Default is `false`.
 * @param includeStatus Whether to include the status of the Members in the result. Default is `false`.
 * @param includeType Whether to include the type of the Members in the result. Default is `false`.
 * @param includeTotalCount Whether to include the total count of Members in the result. Default is `false`.
 * @param includeUser Whether to include user information in the result. Default is `false`.
 * @param includeUserCustom Whether to include custom properties of the user in the result. Default is `false`.
 * @param includeUserType Whether to include the type of the user in the result. Default is `false`.
 * @param includeUserStatus Whether to include the status of the user in the result. Default is `false`.
 *
 * @return An instance of [MemberInclude] with the specified inclusion options.
 */
fun MemberInclude(
    includeCustom: Boolean = false,
    includeStatus: Boolean = false,
    includeType: Boolean = false,
    includeTotalCount: Boolean = false,
    includeUser: Boolean = false,
    includeUserCustom: Boolean = false,
    includeUserType: Boolean = false,
    includeUserStatus: Boolean = false,
) = object : MemberInclude {
    override val includeCustom: Boolean = includeCustom
    override val includeStatus: Boolean = includeStatus
    override val includeType: Boolean = includeType
    override val includeTotalCount: Boolean = includeTotalCount
    override val includeUser: Boolean = includeUser
    override val includeUserCustom: Boolean = includeUserCustom
    override val includeUserType: Boolean = includeUserType
    override val includeUserStatus: Boolean = includeUserStatus
}
