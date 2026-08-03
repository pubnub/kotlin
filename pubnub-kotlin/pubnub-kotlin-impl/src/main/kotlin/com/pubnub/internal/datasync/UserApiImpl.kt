package com.pubnub.internal.datasync

import com.pubnub.api.datasync.UserApi
import com.pubnub.api.endpoints.datasync.user.CreateUser
import com.pubnub.api.endpoints.datasync.user.GetUser
import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.endpoints.datasync.user.PatchUser
import com.pubnub.api.endpoints.datasync.user.RemoveUser
import com.pubnub.api.endpoints.datasync.user.UpdateUser
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.datasync.user.CreateUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.GetUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.GetUsersEndpoint
import com.pubnub.internal.endpoints.datasync.user.PatchUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.RemoveUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.UpdateUserEndpoint

class UserApiImpl(private val pubnub: PubNubImpl) : UserApi {
    override fun get(userId: String): GetUser {
        return GetUserEndpoint(pubnub, userId)
    }

    override fun create(
        entityClassVersion: Int,
        userId: String?,
        entityClass: String?,
        status: String?,
        payload: Any?,
    ): CreateUser {
        return CreateUserEndpoint(
            pubnub = pubnub,
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            userId = userId,
            status = status,
            payload = payload,
        )
    }

    override fun delete(userId: String, ifMatch: String?): RemoveUser {
        return RemoveUserEndpoint(pubnub, userId, ifMatch)
    }

    override fun getAll(
        entityClass: String?,
        entityClassVersion: Int?,
        entityClassLevel: String?,
        filter: String?,
        filterAdvanced: String?,
        sort: String?,
        limit: Int?,
        cursor: String?,
    ): GetUsers {
        return GetUsersEndpoint(
            pubnub = pubnub,
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityClassLevel = entityClassLevel,
            filter = filter,
            filterAdvanced = filterAdvanced,
            sort = sort,
            limit = limit,
            cursor = cursor,
        )
    }

    override fun patch(
        userId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): PatchUser {
        return PatchUserEndpoint(pubnub, userId, operations, ifMatch)
    }

    override fun update(
        userId: String,
        entityClassVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): UpdateUser {
        return UpdateUserEndpoint(
            pubnub = pubnub,
            userId = userId,
            entityClassVersion = entityClassVersion,
            status = status,
            payload = payload,
            ifMatch = ifMatch,
        )
    }
}
