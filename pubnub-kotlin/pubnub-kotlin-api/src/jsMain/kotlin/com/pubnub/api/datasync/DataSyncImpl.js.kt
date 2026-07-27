package com.pubnub.api.datasync

import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity

internal class DataSyncImpl : DataSync {
    override val entity: EntityApi = EntityApiImpl()
}

internal class EntityApiImpl : EntityApi {
    override fun get(entityId: String): GetEntity = throw NotImplementedError("DataSync is not implemented on the JS target")

    override fun create(
        entityClass: String,
        entityClassVersion: Int,
        entityId: String?,
        status: String?,
        payload: Any?,
    ): CreateEntity = throw NotImplementedError("DataSync is not implemented on the JS target")

    override fun delete(entityId: String, ifMatch: String?): RemoveEntity =
        throw NotImplementedError("DataSync is not implemented on the JS target")
}
