package com.pubnub.test

import com.pubnub.api.Endpoint
import com.pubnub.api.JsonElement
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.kmp.CommonPubNub
import kotlinx.datetime.Clock

expect fun<T> createEndpoint(action: () -> T): Endpoint<T>

class FakePubNub(override val configuration: PNConfiguration) : CommonPubNub {

    private val messages: MutableList<PNMessageResult> = mutableListOf()

    override fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?
    ): Endpoint<PNPublishResult> {
        return createEndpoint {
            val tt = Clock.System.now().toEpochMilliseconds() / 1000
            messages.add(
                PNMessageResult(
                    BasePubSubResult(channel, null, tt, null, configuration.userId.value),
                    message as JsonElement,
                    null
                )
            )
            PNPublishResult(tt)
        }
    }

    
    override fun fire(
        channel: String,
        message: Any,
        meta: Any?,
        usePost: Boolean,
        ttl: Int?
    ): Endpoint<PNPublishResult> {
        TODO("Not yet implemented")
    }

    
    override fun signal(channel: String, message: Any): Endpoint<PNPublishResult> {
        TODO("Not yet implemented")
    }

    
    override fun getSubscribedChannels(): List<String> {
        TODO("Not yet implemented")
    }

    
    override fun getSubscribedChannelGroups(): List<String> {
        TODO("Not yet implemented")
    }

    
    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushAddChannelResult> {
        TODO("Not yet implemented")
    }

    
    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushListProvisionsResult> {
        TODO("Not yet implemented")
    }

    
    override fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushRemoveChannelResult> {
        TODO("Not yet implemented")
    }

    
    override fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushRemoveAllChannelsResult> {
        TODO("Not yet implemented")
    }

    
    override fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean
    ): Endpoint<PNFetchMessagesResult> {
        TODO("Not yet implemented")
    }

    
    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): Endpoint<PNDeleteMessagesResult> {
        TODO("Not yet implemented")
    }

    
    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): Endpoint<PNMessageCountResult> {
        TODO("Not yet implemented")
    }

    
    override fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean
    ): Endpoint<PNHereNowResult> {
        TODO("Not yet implemented")
    }

    
    override fun whereNow(uuid: String): Endpoint<PNWhereNowResult> {
        TODO("Not yet implemented")
    }

    
    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
        uuid: String
    ): Endpoint<PNSetStateResult> {
        TODO("Not yet implemented")
    }

    
    override fun getPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        uuid: String
    ): Endpoint<PNGetStateResult> {
        TODO("Not yet implemented")
    }

    
    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
        TODO("Not yet implemented")
    }

    
    override fun addMessageAction(channel: String, messageAction: PNMessageAction): Endpoint<PNAddMessageActionResult> {
        TODO("Not yet implemented")
    }

    
    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): Endpoint<PNRemoveMessageActionResult> {
        TODO("Not yet implemented")
    }

    
    override fun getMessageActions(channel: String, page: PNBoundedPage): Endpoint<PNGetMessageActionsResult> {
        TODO("Not yet implemented")
    }

    
    override fun addChannelsToChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): Endpoint<PNChannelGroupsAddChannelResult> {
        TODO("Not yet implemented")
    }

    
    override fun listChannelsForChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsAllChannelsResult> {
        TODO("Not yet implemented")
    }

    
    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): Endpoint<PNChannelGroupsRemoveChannelResult> {
        TODO("Not yet implemented")
    }

    
    override fun listAllChannelGroups(): Endpoint<PNChannelGroupsListAllResult> {
        TODO("Not yet implemented")
    }

    
    override fun deleteChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsDeleteGroupResult> {
        TODO("Not yet implemented")
    }

    
    override fun grant(
        read: Boolean,
        write: Boolean,
        manage: Boolean,
        delete: Boolean,
        ttl: Int,
        authKeys: List<String>,
        channels: List<String>,
        channelGroups: List<String>,
        uuids: List<String>
    ): Endpoint<PNAccessManagerGrantResult> {
        TODO("Not yet implemented")
    }

    
    override fun grant(
        read: Boolean,
        write: Boolean,
        manage: Boolean,
        delete: Boolean,
        get: Boolean,
        update: Boolean,
        join: Boolean,
        ttl: Int,
        authKeys: List<String>,
        channels: List<String>,
        channelGroups: List<String>,
        uuids: List<String>
    ): Endpoint<PNAccessManagerGrantResult> {
        TODO("Not yet implemented")
    }

    
    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>
    ): Endpoint<PNGrantTokenResult> {
        TODO("Not yet implemented")
    }

    
    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUserId: UserId?,
        spacesPermissions: List<SpacePermissions>,
        usersPermissions: List<UserPermissions>
    ): Endpoint<PNGrantTokenResult> {
        TODO("Not yet implemented")
    }

    
    override fun revokeToken(token: String): Endpoint<Unit> {
        TODO("Not yet implemented")
    }

    
    override fun time(): Endpoint<PNTimeResult> {
        TODO("Not yet implemented")
    }

    
    override fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): Endpoint<PNChannelMetadataArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun getChannelMetadata(channel: String, includeCustom: Boolean): Endpoint<PNChannelMetadataResult> {
        TODO("Not yet implemented")
    }

    
    override fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): Endpoint<PNChannelMetadataResult> {
        TODO("Not yet implemented")
    }

    
    override fun removeChannelMetadata(channel: String): Endpoint<PNRemoveMetadataResult> {
        TODO("Not yet implemented")
    }

    
    override fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): Endpoint<PNUUIDMetadataArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): Endpoint<PNUUIDMetadataResult> {
        TODO("Not yet implemented")
    }

    
    override fun setUUIDMetadata(
        uuid: String?,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): Endpoint<PNUUIDMetadataResult> {
        TODO("Not yet implemented")
    }

    
    override fun removeUUIDMetadata(uuid: String?): Endpoint<PNRemoveMetadataResult> {
        TODO("Not yet implemented")
    }

    
    override fun getMemberships(
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun setMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun removeMemberships(
        channels: List<String>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun manageMemberships(
        channelsToSet: List<ChannelMembershipInput>,
        channelsToRemove: List<String>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun setChannelMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun removeChannelMembers(
        channel: String,
        uuids: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun manageChannelMembers(
        channel: String,
        uuidsToSet: Collection<MemberInput>,
        uuidsToRemove: Collection<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

    
    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): Endpoint<PNListFilesResult> {
        TODO("Not yet implemented")
    }

    
    override fun getFileUrl(channel: String, fileName: String, fileId: String): Endpoint<PNFileUrlResult> {
        TODO("Not yet implemented")
    }

    
    override fun deleteFile(channel: String, fileName: String, fileId: String): Endpoint<PNDeleteFileResult> {
        TODO("Not yet implemented")
    }

    
    override fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?
    ): Endpoint<PNPublishFileMessageResult> {
        TODO("Not yet implemented")
    }

    
    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        TODO("Not yet implemented")
    }

    
    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
        TODO("Not yet implemented")
    }

    override fun setToken(token: String?) {
        TODO("Not yet implemented")
    }
}