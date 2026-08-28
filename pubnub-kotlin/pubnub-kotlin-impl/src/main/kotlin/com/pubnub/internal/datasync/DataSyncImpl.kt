package com.pubnub.internal.datasync

import com.pubnub.api.datasync.DataSync
import com.pubnub.api.endpoints.datasync.channel.CreateChannel
import com.pubnub.api.endpoints.datasync.channel.GetChannel
import com.pubnub.api.endpoints.datasync.channel.GetChannels
import com.pubnub.api.endpoints.datasync.channel.RemoveChannel
import com.pubnub.api.endpoints.datasync.channel.SetChannel
import com.pubnub.api.endpoints.datasync.channel.UpdateChannel
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
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.datasync.channel.CreateChannelEndpoint
import com.pubnub.internal.endpoints.datasync.channel.GetChannelEndpoint
import com.pubnub.internal.endpoints.datasync.channel.GetChannelsEndpoint
import com.pubnub.internal.endpoints.datasync.channel.RemoveChannelEndpoint
import com.pubnub.internal.endpoints.datasync.channel.SetChannelEndpoint
import com.pubnub.internal.endpoints.datasync.channel.UpdateChannelEndpoint
import com.pubnub.internal.endpoints.datasync.entity.CreateEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.GetEntitiesEndpoint
import com.pubnub.internal.endpoints.datasync.entity.GetEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.RemoveEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.SetEntityEndpoint
import com.pubnub.internal.endpoints.datasync.entity.UpdateEntityEndpoint
import com.pubnub.internal.endpoints.datasync.user.CreateUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.GetUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.GetUsersEndpoint
import com.pubnub.internal.endpoints.datasync.user.RemoveUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.SetUserEndpoint
import com.pubnub.internal.endpoints.datasync.user.UpdateUserEndpoint

class DataSyncImpl(private val pubnub: PubNubImpl) : DataSync {
    override fun getEntity(entityId: String): GetEntity {
        return GetEntityEndpoint(pubnub, entityId)
    }

    override fun createEntity(
        className: String,
        classVersion: Int,
        classLevel: PNDataSyncClassLevel?,
        entityId: String?,
        status: String?,
        payload: Any?,
    ): CreateEntity {
        return CreateEntityEndpoint(
            pubnub = pubnub,
            className = className,
            classVersion = classVersion,
            classLevel = classLevel,
            entityId = entityId,
            status = status,
            payload = payload,
        )
    }

    override fun removeEntity(entityId: String, ifMatch: String?): RemoveEntity {
        return RemoveEntityEndpoint(pubnub, entityId, ifMatch)
    }

    override fun getEntities(
        className: String,
        classVersion: Int?,
        classLevel: PNDataSyncClassLevel?,
        filter: String?,
        filterAdvanced: String?,
        sort: List<PNDataSyncSortField>,
        limit: Int?,
        cursor: String?,
    ): GetEntities {
        return GetEntitiesEndpoint(
            pubnub = pubnub,
            className = className,
            classVersion = classVersion,
            classLevel = classLevel,
            filter = filter,
            filterAdvanced = filterAdvanced,
            sort = sort,
            limit = limit,
            cursor = cursor,
        )
    }

    override fun updateEntity(
        entityId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): UpdateEntity {
        return UpdateEntityEndpoint(pubnub, entityId, operations, ifMatch)
    }

    override fun setEntity(
        entityId: String,
        classVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetEntity {
        return SetEntityEndpoint(
            pubnub = pubnub,
            entityId = entityId,
            classVersion = classVersion,
            status = status,
            payload = payload,
            ifMatch = ifMatch,
        )
    }

    override fun getUser(userId: String): GetUser {
        return GetUserEndpoint(pubnub, userId)
    }

    override fun createUser(
        classVersion: Int,
        userId: String?,
        className: String?,
        classLevel: PNDataSyncClassLevel?,
        status: String?,
        payload: Any?,
    ): CreateUser {
        return CreateUserEndpoint(
            pubnub = pubnub,
            className = className,
            classVersion = classVersion,
            classLevel = classLevel,
            userId = userId,
            status = status,
            payload = payload,
        )
    }

    override fun removeUser(userId: String, ifMatch: String?): RemoveUser {
        return RemoveUserEndpoint(pubnub, userId, ifMatch)
    }

    override fun getUsers(
        className: String?,
        classVersion: Int?,
        classLevel: PNDataSyncClassLevel?,
        filter: String?,
        filterAdvanced: String?,
        sort: List<PNDataSyncSortField>,
        limit: Int?,
        cursor: String?,
    ): GetUsers {
        return GetUsersEndpoint(
            pubnub = pubnub,
            className = className,
            classVersion = classVersion,
            classLevel = classLevel,
            filter = filter,
            filterAdvanced = filterAdvanced,
            sort = sort,
            limit = limit,
            cursor = cursor,
        )
    }

    override fun updateUser(
        userId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): UpdateUser {
        return UpdateUserEndpoint(pubnub, userId, operations, ifMatch)
    }

    override fun setUser(
        userId: String,
        classVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetUser {
        return SetUserEndpoint(
            pubnub = pubnub,
            userId = userId,
            classVersion = classVersion,
            status = status,
            payload = payload,
            ifMatch = ifMatch,
        )
    }

    override fun getChannel(channelId: String): GetChannel {
        return GetChannelEndpoint(pubnub, channelId)
    }

    override fun createChannel(
        classVersion: Int,
        channelId: String?,
        className: String?,
        classLevel: PNDataSyncClassLevel?,
        status: String?,
        payload: Any?,
    ): CreateChannel {
        return CreateChannelEndpoint(
            pubnub = pubnub,
            className = className,
            classVersion = classVersion,
            classLevel = classLevel,
            channelId = channelId,
            status = status,
            payload = payload,
        )
    }

    override fun removeChannel(channelId: String, ifMatch: String?): RemoveChannel {
        return RemoveChannelEndpoint(pubnub, channelId, ifMatch)
    }

    override fun getChannels(
        className: String?,
        classVersion: Int?,
        classLevel: PNDataSyncClassLevel?,
        filter: String?,
        filterAdvanced: String?,
        sort: List<PNDataSyncSortField>,
        limit: Int?,
        cursor: String?,
    ): GetChannels {
        return GetChannelsEndpoint(
            pubnub = pubnub,
            className = className,
            classVersion = classVersion,
            classLevel = classLevel,
            filter = filter,
            filterAdvanced = filterAdvanced,
            sort = sort,
            limit = limit,
            cursor = cursor,
        )
    }

    override fun updateChannel(
        channelId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): UpdateChannel {
        return UpdateChannelEndpoint(pubnub, channelId, operations, ifMatch)
    }

    override fun setChannel(
        channelId: String,
        classVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetChannel {
        return SetChannelEndpoint(
            pubnub = pubnub,
            channelId = channelId,
            classVersion = classVersion,
            status = status,
            payload = payload,
            ifMatch = ifMatch,
        )
    }
}
