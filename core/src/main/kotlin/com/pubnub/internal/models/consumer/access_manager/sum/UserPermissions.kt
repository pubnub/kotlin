package com.pubnub.internal.models.consumer.access_manager.sum

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.internal.models.consumer.access_manager.v3.PNGrant
import com.pubnub.internal.models.consumer.access_manager.v3.PNUUIDPatternGrant
import com.pubnub.internal.models.consumer.access_manager.v3.PNUUIDResourceGrant
import com.pubnub.internal.models.consumer.access_manager.v3.PNUserPatternPermissionsGrant
import com.pubnub.internal.models.consumer.access_manager.v3.PNUserPermissionsGrant
import com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant

interface UserPermissions : PNGrant {
    companion object {
        fun id(
            userId: UserId,
            get: Boolean = false,
            update: Boolean = false,
            delete: Boolean = false
        ): UserPermissions = PNUserPermissionsGrant(
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
        ): UserPermissions = PNUserPatternPermissionsGrant(
            id = pattern,
            delete = delete,
            get = get,
            update = update
        )
    }
}

fun UserPermissions.toUuidGrant(): UUIDGrant {
    return when (this) {
        is PNUserPermissionsGrant -> PNUUIDResourceGrant(userPermissions = this)
        is PNUserPatternPermissionsGrant -> PNUUIDPatternGrant(userPermissions = this)
        else -> throw PubNubException(pubnubError = PubNubError.INVALID_ARGUMENTS)
    }
}
