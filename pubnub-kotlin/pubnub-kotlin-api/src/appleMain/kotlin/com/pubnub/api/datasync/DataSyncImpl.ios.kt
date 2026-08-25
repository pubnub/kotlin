package com.pubnub.api.datasync

import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.api.endpoints.datasync.entity.SetEntity
import com.pubnub.api.endpoints.datasync.entity.UpdateEntity
import com.pubnub.api.endpoints.datasync.user.CreateUser
import com.pubnub.api.endpoints.datasync.user.GetUser
import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.endpoints.datasync.user.RemoveUser
import com.pubnub.api.endpoints.datasync.user.SetUser
import com.pubnub.api.endpoints.datasync.user.UpdateUser
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation

internal class DataSyncImpl : DataSync {
    override fun getEntity(entityId: String): GetEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun createEntity(
        entityClass: String,
        entityClassVersion: Int,
        entityId: String?,
        status: String?,
        payload: Any?,
    ): CreateEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun removeEntity(entityId: String, ifMatch: String?): RemoveEntity =
        throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getEntities(
        entityClass: String,
        entityClassVersion: Int?,
        entityClassLevel: String?,
        filter: String?,
        filterAdvanced: String?,
        sort: String?,
        limit: Int?,
        cursor: String?,
    ): GetEntities = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun updateEntity(
        entityId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): UpdateEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun setEntity(
        entityId: String,
        entityClassVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getUser(userId: String): GetUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun createUser(
        entityClassVersion: Int,
        userId: String?,
        entityClass: String?,
        status: String?,
        payload: Any?,
    ): CreateUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun removeUser(userId: String, ifMatch: String?): RemoveUser =
        throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getUsers(
        entityClass: String?,
        entityClassVersion: Int?,
        entityClassLevel: String?,
        filter: String?,
        filterAdvanced: String?,
        sort: String?,
        limit: Int?,
        cursor: String?,
    ): GetUsers = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun updateUser(
        userId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): UpdateUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun setUser(
        userId: String,
        entityClassVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetUser = throw NotImplementedError(NOT_IMPLEMENTED)
}

private const val NOT_IMPLEMENTED = "DataSync is not implemented on the Apple target"
