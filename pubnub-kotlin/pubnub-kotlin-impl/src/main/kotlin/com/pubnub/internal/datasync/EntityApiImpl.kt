package com.pubnub.internal.datasync

import com.pubnub.api.datasync.EntityApi
import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.datasync.entity.CreateEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.GetEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.RemoveEntityEndpoint

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
}
