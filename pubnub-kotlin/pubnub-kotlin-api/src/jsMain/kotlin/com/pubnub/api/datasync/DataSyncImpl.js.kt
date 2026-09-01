package com.pubnub.api.datasync

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

internal class DataSyncImpl : DataSync {
    override fun getEntity(entityId: String): GetEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun createEntity(
        className: String,
        classVersion: Int,
        classLevel: PNDataSyncClassLevel?,
        entityId: String?,
        status: String?,
        payload: Any?,
    ): CreateEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun removeEntity(entityId: String, ifMatch: String?): RemoveEntity =
        throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getEntities(
        className: String,
        classVersion: Int?,
        classLevel: PNDataSyncClassLevel?,
        filterFast: String?,
        filter: String?,
        sort: List<PNDataSyncSortField>,
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
        classVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetEntity = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getUser(userId: String): GetUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun createUser(
        classVersion: Int,
        userId: String?,
        className: String?,
        classLevel: PNDataSyncClassLevel?,
        status: String?,
        payload: Any?,
    ): CreateUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun removeUser(userId: String, ifMatch: String?): RemoveUser =
        throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getUsers(
        className: String?,
        classVersion: Int?,
        classLevel: PNDataSyncClassLevel?,
        filterFast: String?,
        filter: String?,
        sort: List<PNDataSyncSortField>,
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
        classVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetUser = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getChannel(channelId: String): GetChannel = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun createChannel(
        classVersion: Int,
        channelId: String?,
        className: String?,
        classLevel: PNDataSyncClassLevel?,
        status: String?,
        payload: Any?,
    ): CreateChannel = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun removeChannel(channelId: String, ifMatch: String?): RemoveChannel =
        throw NotImplementedError(NOT_IMPLEMENTED)

    override fun getChannels(
        className: String?,
        classVersion: Int?,
        classLevel: PNDataSyncClassLevel?,
        filterFast: String?,
        filter: String?,
        sort: List<PNDataSyncSortField>,
        limit: Int?,
        cursor: String?,
    ): GetChannels = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun updateChannel(
        channelId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String?,
    ): UpdateChannel = throw NotImplementedError(NOT_IMPLEMENTED)

    override fun setChannel(
        channelId: String,
        classVersion: Int,
        status: String?,
        payload: Any?,
        ifMatch: String?,
    ): SetChannel = throw NotImplementedError(NOT_IMPLEMENTED)
}

private const val NOT_IMPLEMENTED = "DataSync is not implemented on the JS target"
