package com.pubnub.api

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.Time
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
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.Uploadable
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("PubNubInterface")
actual interface PubNub {
    actual val configuration: PNConfiguration

    actual fun addListener(listener: EventListener)

    actual fun addListener(listener: StatusListener)

    actual fun removeListener(listener: Listener)

    actual fun removeAllListeners()

    actual fun fire(
        channel: String,
        message: Any,
        meta: Any?,
        usePost: Boolean
    ): Publish

    actual fun signal(channel: String, message: Any, customMessageType: String?): Signal

    actual fun getSubscribedChannels(): List<String>

    actual fun getSubscribedChannelGroups(): List<String>

    actual fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): AddChannelsToPush

    actual fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): ListPushProvisions

    actual fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): RemoveChannelsFromPush

    actual fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): RemoveAllPushChannelsForDevice

    actual fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean,
        includeCustomMessageType: Boolean
    ): FetchMessages

    actual fun deleteMessages(
        channels: List<String>,
        start: Long?,
        end: Long?
    ): DeleteMessages

    actual fun messageCounts(
        channels: List<String>,
        channelsTimetoken: List<Long>
    ): MessageCounts

    actual fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean
    ): HereNow

    actual fun whereNow(uuid: String): WhereNow

    actual fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any
    ): SetState

    actual fun getPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        uuid: String
    ): GetState

    actual fun presence(
        channels: List<String>,
        channelGroups: List<String>,
        connected: Boolean
    )

    actual fun addMessageAction(
        channel: String,
        messageAction: PNMessageAction
    ): AddMessageAction

    actual fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): RemoveMessageAction

    actual fun getMessageActions(
        channel: String,
        page: PNBoundedPage
    ): GetMessageActions

    actual fun addChannelsToChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): AddChannelChannelGroup

    actual fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup

    actual fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): RemoveChannelChannelGroup

    actual fun listAllChannelGroups(): ListAllChannelGroup

    actual fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup

    actual fun grantToken(
        ttl: Int,
        meta: CustomObject?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>
    ): GrantToken

    actual fun revokeToken(token: String): RevokeToken

    actual fun time(): Time

    actual fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): GetAllChannelMetadata

    actual fun getChannelMetadata(
        channel: String,
        includeCustom: Boolean
    ): GetChannelMetadata

    actual fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: CustomObject?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): SetChannelMetadata

    actual fun removeChannelMetadata(channel: String): RemoveChannelMetadata

    actual fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): GetAllUUIDMetadata

    actual fun getUUIDMetadata(
        uuid: String?,
        includeCustom: Boolean
    ): GetUUIDMetadata

    actual fun setUUIDMetadata(
        uuid: String?,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: CustomObject?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): SetUUIDMetadata

    actual fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata

    // deprecated
    actual fun getMemberships(
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean
    ): GetMemberships

    actual fun getMemberships(
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude,
    ): GetMemberships

    // deprecated
    actual fun setMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean
    ): ManageMemberships

    actual fun setMemberships(
        channels: List<ChannelMembershipInput>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude,
    ): ManageMemberships

    // deprecated
    actual fun removeMemberships(
        channels: List<String>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean
    ): ManageMemberships

    actual fun removeMemberships(
        channels: List<String>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude
    ): ManageMemberships

    // deprecated
    actual fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
        includeType: Boolean
    ): GetChannelMembers

    actual fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): GetChannelMembers

    // deprecated
    actual fun setChannelMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
        includeType: Boolean
    ): ManageChannelMembers


    actual fun setChannelMembers(
        channel: String,
        users: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): ManageChannelMembers

    // deprecated
    actual fun removeChannelMembers(
        channel: String,
        uuids: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
        includeType: Boolean
    ): ManageChannelMembers


    actual fun removeChannelMembers(
        channel: String,
        userIds: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): ManageChannelMembers

    actual fun listFiles(
        channel: String,
        limit: Int?,
        next: PNPage.PNNext?
    ): ListFiles

    actual fun getFileUrl(
        channel: String,
        fileName: String,
        fileId: String
    ): GetFileUrl

    actual fun sendFile(
        channel: String,
        fileName: String,
        inputStream: Uploadable,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?,
        customMessageType: String?
    ): SendFile

    actual fun downloadFile(
        channel: String,
        fileName: String,
        fileId: String,
        cipherKey: String?
    ): DownloadFile

    actual fun deleteFile(
        channel: String,
        fileName: String,
        fileId: String
    ): DeleteFile

    actual fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        customMessageType: String?
    ): PublishFileMessage

    actual fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    )

    actual fun unsubscribe(
        channels: List<String>,
        channelGroups: List<String>
    )

    actual fun unsubscribeAll()

    actual fun setToken(token: String?)

    actual fun getToken(): String?

    actual fun destroy()

    actual fun channel(name: String): Channel

    actual fun channelGroup(name: String): ChannelGroup

    actual fun channelMetadata(id: String): ChannelMetadata

    actual fun userMetadata(id: String): UserMetadata

    actual fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet

    actual fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions
    ): SubscriptionSet

    actual fun parseToken(token: String): PNToken

    actual fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?,
        customMessageType: String?
    ): Publish
}
