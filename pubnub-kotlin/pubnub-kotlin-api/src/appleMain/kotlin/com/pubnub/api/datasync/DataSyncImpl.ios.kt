package com.pubnub.api.datasync

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

internal class DataSyncImpl : DataSync {
    override val entity: EntityApi = EntityApiImpl()
    override val user: UserApi = UserApiImpl()
}

private const val NOT_IMPLEMENTED = "DataSync is not implemented on the Apple target"

internal class EntityApiImpl : EntityApi {
    override fun get(entityId: String): GetEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun create(
        entityClass: String,
        entityClassVersion: Int,
        entityId: String?,
        status: String?,
        payload: Any?,
    ): CreateEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun delete(entityId: String, ifMatch: String?): RemoveEntity =
        throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getAll(
        entityClass: String,
        entityClassVersion: Int?,
        filter: String?,
        filterAdvanced: String?,
        sort: String?,
        limit: Int?,
        cursor: String?,
    ): GetEntities = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun patch(
        entityId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): PatchEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun update(
        entityId: String,
        entityClassVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): UpdateEntity = throw NotImplementedError(NOT_IMPLEMENTED)
}

internal class UserApiImpl : UserApi {
    override fun get(userId: String): GetUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun create(
        entityClassVersion: Int,
        userId: String?,
        entityClass: String?,
        status: String?,
        payload: Any?,
    ): CreateUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun delete(userId: String, ifMatch: String?): RemoveUser =
        throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getAll(
        entityClass: String?,
        entityClassVersion: Int?,
        entityClassLevel: String?,
        filter: String?,
        filterAdvanced: String?,
        sort: String?,
        limit: Int?,
        cursor: String?,
    ): GetUsers = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun patch(
        userId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): PatchUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun update(
        userId: String,
        entityClassVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): UpdateUser = throw NotImplementedError(NOT_IMPLEMENTED)
}
