package com.pubnub.api.models.consumer.access_manager.sum

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUUIDPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUUIDResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUserIdPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUserIdResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant

interface UserIdGrant : PNGrant {
    companion object {
        fun id(
            userId: UserId,
            get: Boolean = false,
            update: Boolean = false,
            delete: Boolean = false
        ): UserIdGrant = PNUserIdResourceGrant(
            id = userId.value,
            delete = delete,
            get = get,
            update = update
        )

        fun pattern(
            pattern: String,
            get: Boolean = false,
            update: Boolean = false,
            delete: Boolean = false
        ): UserIdGrant = PNUserIdPatternGrant(
            id = pattern,
            delete = delete,
            get = get,
            update = update
        )
    }
}

fun UserIdGrant.toUuidGrant(): UUIDGrant {
    return when (this) {
        is PNUserIdResourceGrant -> PNUUIDResourceGrant(userIdGrant = this)
        is PNUserIdPatternGrant -> PNUUIDPatternGrant(userIdGrant = this)
        else -> throw PubNubException(pubnubError = PubNubError.INVALID_ARGUMENTS)
    }
}
