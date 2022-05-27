package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNUUIDWithCustom

data class UserIdWithCustom(
    val userId: String,
    val custom: Map<String, Any>? = null
)

fun List<UserIdWithCustom>.toPNUUIDWithCustomList(): List<PNUUIDWithCustom> {
    return map { userIdWithCustom -> userIdWithCustom.toPNUUIDWithCustom() }
}

fun UserIdWithCustom.toPNUUIDWithCustom(): PNUUIDWithCustom {
    return PNUUIDWithCustom(uuid = userId, custom = custom)
}