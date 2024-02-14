package com.pubnub.api

import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.endpoints.History
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.access.Grant
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.DownloadFile
import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadata
import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.api.endpoints.objects.member.GetChannelMembers
import com.pubnub.api.endpoints.objects.member.ManageChannelMembers
import com.pubnub.api.endpoints.objects.membership.GetMemberships
import com.pubnub.api.endpoints.objects.membership.ManageMemberships
import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata
import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.endpoints.presence.WhereNow
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.models.consumer.objects.toInternal
import com.pubnub.internal.models.consumer.objects.toInternalChannelGrants
import com.pubnub.internal.models.consumer.objects.toInternalChannelGroupGrants
import com.pubnub.internal.models.consumer.objects.toInternalChannelMemberships
import com.pubnub.internal.models.consumer.objects.toInternalMemberInputs
import com.pubnub.internal.models.consumer.objects.toInternalSortKeys
import com.pubnub.internal.models.consumer.objects.toInternalSpacePermissions
import com.pubnub.internal.models.consumer.objects.toInternalUserPermissions
import com.pubnub.internal.models.consumer.objects.toInternalUuidGrants
import java.io.InputStream

class PubNub(
    val configuration: PNConfiguration,
) : BasePubNub(configuration) {
    companion object {
        fun generateUUID() = BasePubNub.generateUUID()
    }

    fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ): Publish {
        return Publish(
            pubNubImpl.publish(
                channel,
                message,
                meta,
                shouldStore,
                usePost,
                replicate,
                ttl
            )
        )
    }

    fun fire(
        channel: String,
        message: Any,
        meta: Any? = null,
        usePost: Boolean = false,
        ttl: Int? = null
    ): Publish {
        return Publish(pubNubImpl.fire(channel, message, meta, usePost, ttl))
    }

    fun signal(channel: String, message: Any): Signal {
        return Signal(pubNubImpl.signal(channel, message))
    }

    fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): AddChannelsToPush {
        return AddChannelsToPush(
            pubNubImpl.addPushNotificationsOnChannels(
                pushType, channels, deviceId, topic, environment
            )
        )
    }

    fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): ListPushProvisions {
        return ListPushProvisions(pubNubImpl.auditPushChannelProvisions(pushType, deviceId, topic, environment))
    }

    fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): RemoveChannelsFromPush {
        return RemoveChannelsFromPush(
            pubNubImpl.removePushNotificationsFromChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment
            )
        )
    }

    fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): RemoveAllPushChannelsForDevice {
        return RemoveAllPushChannelsForDevice(
            pubNubImpl.removeAllPushNotificationsFromDeviceWithPushToken(pushType, deviceId, topic, environment)
        )
    }

    fun history(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        count: Int = com.pubnub.internal.endpoints.History.MAX_COUNT,
        reverse: Boolean = false,
        includeTimetoken: Boolean = false,
        includeMeta: Boolean = false
    ): History {
        return History(pubNubImpl.history(channel, start, end, count, reverse, includeTimetoken, includeMeta))
    }

    fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage = PNBoundedPage(),
        includeUUID: Boolean = true,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false,
        includeMessageType: Boolean = true
    ): FetchMessages {
        return FetchMessages(
            pubNubImpl.fetchMessages(
                channels,
                page,
                includeUUID,
                includeMeta,
                includeMessageActions,
                includeMessageType
            )
        )
    }

    fun deleteMessages(
        channels: List<String>,
        start: Long? = null,
        end: Long? = null
    ): DeleteMessages {
        return DeleteMessages(pubNubImpl.deleteMessages(channels, start, end))
    }

    fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts {
        return MessageCounts(pubNubImpl.messageCounts(channels, channelsTimetoken))
    }

    fun hereNow(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        includeState: Boolean = false,
        includeUUIDs: Boolean = true
    ): HereNow {
        return HereNow(pubNubImpl.hereNow(channels, channelGroups, includeState, includeUUIDs))
    }

    fun whereNow(uuid: String = configuration.userId.value): WhereNow {
        return WhereNow(pubNubImpl.whereNow(uuid))
    }

    fun setPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        state: Any,
        uuid: String = configuration.userId.value
    ): SetState {
        return SetState(pubNubImpl.setPresenceState(channels, channelGroups, state, uuid))
    }

    fun getPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        uuid: String = configuration.userId.value
    ): GetState {
        return GetState(pubNubImpl.getPresenceState(channels, channelGroups, uuid))
    }

    fun addMessageAction(
        channel: String,
        messageAction: PNMessageAction
    ): com.pubnub.api.endpoints.message_actions.AddMessageAction {
        return AddMessageAction(pubNubImpl.addMessageAction(channel, messageAction))
    }

    fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): RemoveMessageAction {
        return RemoveMessageAction(pubNubImpl.removeMessageAction(channel, messageTimetoken, actionTimetoken))
    }

    fun getMessageActions(
        channel: String,
        page: PNBoundedPage = PNBoundedPage()
    ): GetMessageActions {
        return GetMessageActions(pubNubImpl.getMessageActions(channel, page))
    }

    fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup {
        return AddChannelChannelGroup(
            pubNubImpl.addChannelsToChannelGroup(channels, channelGroup)
        )
    }

    fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        return AllChannelsChannelGroup(
            pubNubImpl.listChannelsForChannelGroup(channelGroup)
        )
    }

    fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): RemoveChannelChannelGroup {
        return RemoveChannelChannelGroup(
            pubNubImpl.removeChannelsFromChannelGroup(channels, channelGroup)
        )
    }

    fun listAllChannelGroups(): ListAllChannelGroup {
        return ListAllChannelGroup(pubNubImpl.listAllChannelGroups())
    }

    fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        return DeleteChannelGroup(pubNubImpl.deleteChannelGroup(channelGroup))
    }

    fun grant(
        read: Boolean = false,
        write: Boolean = false,
        manage: Boolean = false,
        delete: Boolean = false,
        ttl: Int = -1,
        authKeys: List<String> = emptyList(),
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ): Grant = Grant(pubNubImpl.grant(read, write, manage, delete, ttl, authKeys, channels, channelGroups))

    fun grantToken(
        ttl: Int,
        meta: Any? = null,
        authorizedUUID: String? = null,
        channels: List<ChannelGrant> = emptyList(),
        channelGroups: List<ChannelGroupGrant> = emptyList(),
        uuids: List<UUIDGrant> = emptyList()
    ): GrantToken {
        return GrantToken(
            pubNubImpl.grantToken(
                ttl,
                meta,
                authorizedUUID,
                channels.toInternalChannelGrants(),
                channelGroups.toInternalChannelGroupGrants(),
                uuids.toInternalUuidGrants()
            )
        )
    }

    fun grantToken(
        ttl: Int,
        meta: Any? = null,
        authorizedUserId: UserId? = null,
        spacesPermissions: List<SpacePermissions> = emptyList(),
        usersPermissions: List<UserPermissions> = emptyList()
    ): GrantToken {
        return GrantToken(
            pubNubImpl.grantToken(
                ttl,
                meta,
                authorizedUserId,
                spacesPermissions.toInternalSpacePermissions(),
                usersPermissions.toInternalUserPermissions(),
            )
        )
    }

    fun revokeToken(token: String): RevokeToken {
        return RevokeToken(
            pubNubImpl.revokeToken(token)
        )
    }

    fun time(): Time {
        return Time(pubNubImpl.time())
    }

    fun getAllChannelMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ): GetAllChannelMetadata {
        return GetAllChannelMetadata(
            pubNubImpl.getAllChannelMetadata(
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom
            )
        )
    }

    fun getChannelMetadata(channel: String, includeCustom: Boolean = false): GetChannelMetadata {
        return GetChannelMetadata(
            pubNubImpl.getChannelMetadata(channel, includeCustom)
        )
    }

    fun setChannelMetadata(
        channel: String,
        name: String? = null,
        description: String? = null,
        custom: Any? = null,
        includeCustom: Boolean = false,
        type: String? = null,
        status: String? = null
    ): SetChannelMetadata {
        return SetChannelMetadata(
            pubNubImpl.setChannelMetadata(channel, name, description, custom, includeCustom, type, status)
        )
    }

    fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        return RemoveChannelMetadata(
            pubNubImpl.removeChannelMetadata(channel)
        )
    }

    fun getAllUUIDMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ): GetAllUUIDMetadata {
        return GetAllUUIDMetadata(
            pubNubImpl.getAllUUIDMetadata(limit, page, filter, sort.toInternalSortKeys(), includeCount, includeCustom)
        )
    }

    fun getUUIDMetadata(
        uuid: String? = null,
        includeCustom: Boolean = false
    ): GetUUIDMetadata {
        return GetUUIDMetadata(
            pubNubImpl.getUUIDMetadata(uuid, includeCustom)
        )
    }

    fun setUUIDMetadata(
        uuid: String? = null,
        name: String? = null,
        externalId: String? = null,
        profileUrl: String? = null,
        email: String? = null,
        custom: Any? = null,
        includeCustom: Boolean = false,
        type: String? = null,
        status: String? = null
    ): SetUUIDMetadata {
        return SetUUIDMetadata(
            pubNubImpl.setUUIDMetadata(uuid, name, externalId, profileUrl, email, custom, includeCustom, type, status)
        )
    }

    fun removeUUIDMetadata(uuid: String? = null): RemoveUUIDMetadata {
        return RemoveUUIDMetadata(
            pubNubImpl.removeUUIDMetadata(uuid)
        )
    }

    fun getMemberships(
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): GetMemberships {
        return GetMemberships(
            pubNubImpl.getMemberships(
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun setMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null,
    ): ManageMemberships {
        return ManageMemberships(
            pubNubImpl.setMemberships(
                channels.toInternalChannelMemberships(),
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun removeMemberships(
        channels: List<String>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships {
        return ManageMemberships(
            pubNubImpl.removeMemberships(
                channels,
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun manageMemberships(
        channelsToSet: List<ChannelMembershipInput>,
        channelsToRemove: List<String>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null,
    ): ManageMemberships {
        return ManageMemberships(
            pubNubImpl.manageMemberships(
                channelsToSet.toInternalChannelMemberships(),
                channelsToRemove,
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun getChannelMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): GetChannelMembers {
        return GetChannelMembers(
            pubNubImpl.getChannelMembers(
                channel,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    @Deprecated(
        replaceWith = ReplaceWith(
            "fetchMessages(channels = channels, page = PNBoundedPage(start = start, end = end, limit = maximumPerChannel),includeMeta = includeMeta, includeMessageActions = includeMessageActions, includeMessageType = includeMessageType)",
            "com.pubnub.api.models.consumer.PNBoundedPage"
        ),
        level = DeprecationLevel.ERROR,
        message = "Use fetchMessages(String, PNBoundedPage, Boolean, Boolean, Boolean) instead"
    )
    fun fetchMessages(
        channels: List<String>,
        maximumPerChannel: Int = 0,
        start: Long? = null,
        end: Long? = null,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false,
        includeMessageType: Boolean = true
    ): FetchMessages {
        return FetchMessages(
            pubNubImpl.fetchMessages(
                channels, maximumPerChannel, start, end, includeMeta, includeMessageActions, includeMessageType
            )
        )
    }

    @Deprecated(
        replaceWith = ReplaceWith(
            "getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))",
            "com.pubnub.api.models.consumer.PNBoundedPage"
        ),
        level = DeprecationLevel.ERROR,
        message = "Use getMessageActions(String, PNBoundedPage) instead"
    )
    fun getMessageActions(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        limit: Int? = null
    ): GetMessageActions {
        return GetMessageActions(
            pubNubImpl.getMessageActions(channel, start, end, limit)
        )
    }

    @Deprecated(
        replaceWith = ReplaceWith(
            "setMemberships(channels = channels, uuid = uuid, limit = limit, " +
                "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                "includeChannelDetails = includeChannelDetails)"
        ),
        level = DeprecationLevel.ERROR,
        message = "Use setMemberships instead"
    )
    fun addMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships {
        return ManageMemberships(
            pubNubImpl.addMemberships(
                channels.toInternalChannelMemberships(),
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    @Deprecated(
        "Use getChannelMembers instead",
        replaceWith = ReplaceWith("getChannelMembers(channel = channel, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)"),
        level = DeprecationLevel.ERROR
    )
    fun getMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): GetChannelMembers {
        return GetChannelMembers(
            pubNubImpl.getMembers(
                channel,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    @Deprecated(
        "Use setChannelMembers instead",
        replaceWith = ReplaceWith("setChannelMembers(channel = channel, uuids = uuids, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)"),
        level = DeprecationLevel.ERROR
    )
    fun addMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers {
        return ManageChannelMembers(
            pubNubImpl.addMembers(
                channel,
                uuids.toInternalMemberInputs(),
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun setChannelMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
    ): ManageChannelMembers {
        return ManageChannelMembers(
            pubNubImpl.setChannelMembers(
                channel,
                uuids.toInternalMemberInputs(),
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    @Deprecated(
        "Use removeChannelMembers instead",
        replaceWith = ReplaceWith("removeChannelMembers(channel = channel, uuids = uuids, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)"),
        level = DeprecationLevel.ERROR
    )
    fun removeMembers(
        channel: String,
        uuids: List<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers {
        return ManageChannelMembers(
            pubNubImpl.removeMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun removeChannelMembers(
        channel: String,
        uuids: List<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers {
        return ManageChannelMembers(
            pubNubImpl.removeChannelMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun manageChannelMembers(
        channel: String,
        uuidsToSet: Collection<MemberInput>,
        uuidsToRemove: Collection<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
    ): ManageChannelMembers {
        return ManageChannelMembers(
            pubNubImpl.manageChannelMembers(
                channel,
                uuidsToSet.toInternalMemberInputs(),
                uuidsToRemove,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun sendFile(
        channel: String,
        fileName: String,
        inputStream: InputStream,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null,
        cipherKey: String? = null
    ): SendFile {
        return SendFile(
            pubNubImpl.sendFile(
                channel, fileName, inputStream, message, meta, ttl, shouldStore, cipherKey
            )
        )
    }

    fun listFiles(
        channel: String,
        limit: Int? = null,
        next: PNPage.PNNext? = null
    ): ListFiles {
        return ListFiles(
            pubNubImpl.listFiles(channel, limit, next)
        )
    }

    fun getFileUrl(channel: String, fileName: String, fileId: String): GetFileUrl {
        return GetFileUrl(
            pubNubImpl.getFileUrl(channel, fileName, fileId)
        )
    }

    fun downloadFile(channel: String, fileName: String, fileId: String, cipherKey: String? = null): DownloadFile {
        return DownloadFile(
            pubNubImpl.downloadFile(channel, fileName, fileId, cipherKey)
        )
    }

    fun deleteFile(channel: String, fileName: String, fileId: String): DeleteFile {
        return DeleteFile(
            pubNubImpl.deleteFile(channel, fileName, fileId)
        )
    }

    fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null
    ): PublishFileMessage {
        return PublishFileMessage(
            pubNubImpl.publishFileMessage(channel, fileName, fileId, message, meta, ttl, shouldStore)
        )
    }

    /**
     * Causes the client to create an open TCP socket to the PubNub Real-Time Network and begin listening for messages
     * on a specified channel.
     *
     * To subscribe to a channel the client must send the appropriate [PNConfiguration.subscribeKey] at initialization.
     *
     * By default, a newly subscribed client will only receive messages published to the channel
     * after the `subscribe()` call completes.
     *
     * If a client gets disconnected from a channel, it can automatically attempt to reconnect to that channel
     * and retrieve any available messages that were missed during that period.
     * This can be achieved by setting [PNConfiguration.reconnectionPolicy] to [PNReconnectionPolicy.LINEAR], when
     * initializing the client.
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     * @param withPresence Also subscribe to related presence channel.
     * @param withTimetoken A timetoken to start the subscribe loop from.
     */
    fun subscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        withPresence: Boolean = false,
        withTimetoken: Long = 0L
    ) = pubNubImpl.subscribe(channels, channelGroups, withPresence, withTimetoken)

    /**
     * When subscribed to a single channel, this function causes the client to issue a leave from the channel
     * and close any open socket to the PubNub Network.
     *
     * For multiplexed channels, the specified channel(s) will be removed and the socket remains open
     * until there are no more channels remaining in the list.
     *
     * * **WARNING**
     * Unsubscribing from all the channel(s) and then subscribing to a new channel Y isn't the same as
     * Subscribing to channel Y and then unsubscribing from the previously subscribed channel(s).
     *
     * Unsubscribing from all the channels resets the timetoken and thus,
     * there could be some gaps in the subscription that may lead to a message loss.
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) = pubNubImpl.unsubscribe(
        channels, channelGroups
    )

    /**
     * Unsubscribe from all channels and all channel groups
     */
    fun unsubscribeAll() = pubNubImpl.unsubscribeAll()

    /**
     * Queries the local subscribe loop for channels currently in the mix.
     *
     * @return A list of channels the client is currently subscribed to.
     */
    fun getSubscribedChannels(): List<String> = pubNubImpl.getSubscribedChannels()

    /**
     * Queries the local subscribe loop for channel groups currently in the mix.
     *
     * @return A list of channel groups the client is currently subscribed to.
     */
    fun getSubscribedChannelGroups(): List<String> = pubNubImpl.getSubscribedChannelGroups()

    /**
     * Track the online and offline status of users and devices in real time and store custom state information.
     * When you have Presence enabled, PubNub automatically creates a presence channel for each channel.
     *
     * Subscribing to a presence channel or presence channel group will only return presence events
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    fun presence(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        connected: Boolean = false
    ) = pubNubImpl.presence(channels, channelGroups, connected)

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
     *
     * @param inputString String to be decrypted.
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    fun decrypt(inputString: String): String = pubNubImpl.decrypt(inputString)

    /**
     * Perform Cryptographic decryption of an input string using a cipher key.
     *
     * @param inputString String to be decrypted.
     * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    fun decrypt(inputString: String, cipherKey: String? = null): String = pubNubImpl.decrypt(inputString, cipherKey)

    /**
     * Perform Cryptographic decryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for decryption. If not provided [PNConfiguration.cipherKey] is used.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed decryption.
     */
    fun decryptInputStream(inputStream: InputStream, cipherKey: String? = null): InputStream =
        pubNubImpl.decryptInputStream(inputStream, cipherKey)

    /**
     * Perform Cryptographic encryption of an input string and a cipher key.
     *
     * @param inputString String to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the encryption of `inputString` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    fun encrypt(inputString: String, cipherKey: String? = null): String = pubNubImpl.encrypt(inputString, cipherKey)

    /**
     * Perform Cryptographic encryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. If not provided [PNConfiguration.cipherKey] is used.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    fun encryptInputStream(inputStream: InputStream, cipherKey: String? = null): InputStream =
        pubNubImpl.encryptInputStream(inputStream, cipherKey)

    /**
     * Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status]
     */
    fun reconnect(timetoken: Long = 0L) = pubNubImpl.reconnect(timetoken)

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    fun disconnect() = pubNubImpl.disconnect()

    fun parseToken(token: String): PNToken {
        return pubNubImpl.parseToken(token)
    }

    fun setToken(token: String?) {
        return pubNubImpl.setToken(token)
    }
}
