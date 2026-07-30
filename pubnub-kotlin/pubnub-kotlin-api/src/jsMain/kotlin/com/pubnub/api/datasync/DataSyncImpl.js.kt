package com.pubnub.api.datasync

import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.PatchEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.api.endpoints.datasync.entity.UpdateEntity
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation

internal class DataSyncImpl : DataSync {
    override val entity: EntityApi = EntityApiImpl()
}

private const val NOT_IMPLEMENTED = "DataSync is not implemented on the JS target"

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
