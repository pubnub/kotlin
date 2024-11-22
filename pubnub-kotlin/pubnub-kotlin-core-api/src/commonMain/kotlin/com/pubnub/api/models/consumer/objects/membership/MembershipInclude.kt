package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.member.Include

interface MembershipInclude : Include {
    val includeChannel: Boolean
    val includeChannelCustom: Boolean
    val includeChannelType: Boolean
    val includeChannelStatus: Boolean
}
