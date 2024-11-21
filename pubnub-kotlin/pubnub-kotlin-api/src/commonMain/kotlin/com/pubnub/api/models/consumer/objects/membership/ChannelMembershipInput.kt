package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.kmp.CustomObject

interface ChannelMembershipInput {
    val channel: String
    val custom: CustomObject?
    val status: String?
    val type: String?
}
