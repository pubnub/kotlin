package com.pubnub.api.models.consumer.objects.member

import com.pubnub.kmp.CustomObject

interface MemberInput {
    val uuid: String // todo change to userId?
    val custom: CustomObject?
    val status: String?
    val type: String?
}
