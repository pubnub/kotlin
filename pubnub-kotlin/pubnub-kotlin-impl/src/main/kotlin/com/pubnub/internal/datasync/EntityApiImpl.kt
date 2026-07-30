package com.pubnub.internal.datasync

import com.pubnub.api.datasync.EntityApi
import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.PatchEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.api.endpoints.datasync.entity.UpdateEntity
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.datasync.entity.CreateEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.GetEntitiesEndpoint
import com.pubnub.internal.endpoints.datasync.entity.GetEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.PatchEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.RemoveEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.UpdateEntityEndpoint

class EntityApiImpl(private val pubnub: PubNubImpl) : EntityApi {
    override fun get(entityId: String): GetEntity {
        return GetEntityEndpoint(pubnub, entityId)
    }

    override fun create(
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

    override fun delete(entityId: String, ifMatch: String?): RemoveEntity {
        return RemoveEntityEndpoint(pubnub, entityId, ifMatch)
    }

    override fun getAll(
        entityClass: String,
        entityClassVersion: Int?,
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
            filter = filter,
            filterAdvanced = filterAdvanced,
            sort = sort,
            limit = limit,
            cursor = cursor,
        )
    }

    override fun patch(
        entityId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): PatchEntity {
        return PatchEntityEndpoint(pubnub, entityId, operations, ifMatch)
    }

    override fun update(
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
}
