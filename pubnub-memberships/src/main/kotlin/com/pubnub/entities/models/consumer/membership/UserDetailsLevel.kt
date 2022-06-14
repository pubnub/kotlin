package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel

enum class UserDetailsLevel {
    USER,
    USER_WITH_CUSTOM
}

internal fun UserDetailsLevel.toPNUUIDDetailsLevel(): PNUUIDDetailsLevel {
    return when (this) {
        UserDetailsLevel.USER -> PNUUIDDetailsLevel.UUID
        UserDetailsLevel.USER_WITH_CUSTOM -> PNUUIDDetailsLevel.UUID_WITH_CUSTOM
    }
}
