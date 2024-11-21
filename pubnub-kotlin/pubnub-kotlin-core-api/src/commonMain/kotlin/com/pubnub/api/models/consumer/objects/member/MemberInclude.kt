package com.pubnub.api.models.consumer.objects.member

abstract class Include(
    open val includeCustom: Boolean = false,
    open val includeStatus: Boolean = false,
    open val includeType: Boolean = false,
    open val includeTotalCount: Boolean = false,
)

class MemberInclude(
    override val includeCustom: Boolean = false,
    override val includeStatus: Boolean = false,
    override val includeType: Boolean = false,
    override val includeTotalCount: Boolean = false,
    val includeUser: Boolean = false,
    val includeUserCustom: Boolean = false,
    val includeUserType: Boolean = false,
    val includeUserStatus: Boolean = false,
) : Include()
