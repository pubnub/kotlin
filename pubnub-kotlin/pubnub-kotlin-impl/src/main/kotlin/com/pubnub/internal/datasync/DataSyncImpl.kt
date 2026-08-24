package com.pubnub.internal.datasync

import com.pubnub.api.datasync.DataSync
import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.PatchEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.api.endpoints.datasync.entity.UpdateEntity
import com.pubnub.api.endpoints.datasync.user.CreateUser
import com.pubnub.api.endpoints.datasync.user.GetUser
import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.endpoints.datasync.user.PatchUser
import com.pubnub.api.endpoints.datasync.user.RemoveUser
import com.pubnub.api.endpoints.datasync.user.UpdateUser
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.datasync.entity.CreateEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.GetEntitiesEndpoint
import com.pubnub.internal.endpoints.datasync.entity.GetEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.PatchEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.RemoveEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.UpdateEntityEndpoint
import com.pubnub.internal.endpoints.datasync.user.CreateUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.GetUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.GetUsersEndpoint
import com.pubnub.internal.endpoints.datasync.user.PatchUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.RemoveUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.UpdateUserEndpoint

class DataSyncImpl(private val pubnub: PubNubImpl) : DataSync {
    override fun getEntity(entityId: String): GetEntity {
        return GetEntityEndpoint(pubnub, entityId)
    }

    override fun createEntity(
        entityClass: String,
        entityClassVersion: Int,
        entityId: String?,
        status: String?,
        payload: Any?,
    ): CreateEntity {
        return CreateEntityEndpoint(
            pubnub = pubnub,
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = entityId,
            status = status,
            payload = payload,
        )
    }

    override fun removeEntity(entityId: String, ifMatch: String?): RemoveEntity {
        return RemoveEntityEndpoint(pubnub, entityId, ifMatch)
    }

    override fun getEntities(
        entityClass: String,
        entityClassVersion: Int?,
        entityClassLevel: String?,
        filter: String?,
        filterAdvanced: String?,
        sort: String?,
        limit: Int?,
        cursor: String?,
    ): GetEntities {
        return GetEntitiesEndpoint(
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

    override fun patchEntity(
        entityId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): PatchEntity {
        return PatchEntityEndpoint(pubnub, entityId, operations, ifMatch)
    }

    override fun updateEntity(
        entityId: String,
        entityClassVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): UpdateEntity {
        return UpdateEntityEndpoint(
            pubnub = pubnub,
            entityId = entityId,
            entityClassVersion = entityClassVersion,
            status = status,
            payload = payload,
            ifMatch = ifMatch,
        )
    }

    override fun getUser(userId: String): GetUser {
        return GetUserEndpoint(pubnub, userId)
    }

    override fun createUser(
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

    override fun removeUser(userId: String, ifMatch: String?): RemoveUser {
        return RemoveUserEndpoint(pubnub, userId, ifMatch)
    }

    override fun getUsers(
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

    override fun patchUser(
        userId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): PatchUser {
        return PatchUserEndpoint(pubnub, userId, operations, ifMatch)
    }

    override fun updateUser(
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
