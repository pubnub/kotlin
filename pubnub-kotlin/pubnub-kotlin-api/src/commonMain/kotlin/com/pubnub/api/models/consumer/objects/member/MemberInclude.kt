package com.pubnub.api.models.consumer.objects.member

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
    override val includeCustom: Boolean =  includeCustom
    override val includeStatus: Boolean = includeStatus
    override val includeType: Boolean = includeType
    override val includeTotalCount: Boolean = includeTotalCount
    override val includeUser: Boolean = includeUser
    override val includeUserCustom: Boolean = includeUserCustom
    override val includeUserType: Boolean = includeUserType
    override val includeUserStatus: Boolean = includeUserStatus
}
