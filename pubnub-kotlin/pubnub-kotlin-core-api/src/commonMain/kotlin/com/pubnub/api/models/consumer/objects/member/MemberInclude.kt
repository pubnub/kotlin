package com.pubnub.api.models.consumer.objects.member

interface Include {
    val includeCustom: Boolean
    val includeStatus: Boolean
    val includeType: Boolean
    val includeTotalCount: Boolean
}

interface MemberInclude : Include {
    val includeUser: Boolean
    val includeUserCustom: Boolean
    val includeUserType: Boolean
    val includeUserStatus: Boolean
}
