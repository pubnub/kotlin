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
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.Uploadable

expect interface PubNub {
    val configuration: PNConfiguration

    fun addListener(listener: EventListener)

    fun addListener(listener: StatusListener)

    fun removeListener(listener: Listener)

    fun removeAllListeners()

    fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean = true,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null,
    ): Publish

    fun fire(
        channel: String,
        message: Any,
        meta: Any? = null,
        usePost: Boolean = false,
    ): Publish

    fun signal(
        channel: String,
        message: Any,
    ): Signal

    fun getSubscribedChannels(): List<String>

    fun getSubscribedChannelGroups(): List<String>

    fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT,
    ): AddChannelsToPush

    fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT,
    ): ListPushProvisions

    fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT,
    ): RemoveChannelsFromPush

    fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT,
    ): RemoveAllPushChannelsForDevice

    fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage = PNBoundedPage(),
        includeUUID: Boolean = true,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false,
        includeMessageType: Boolean = true,
    ): FetchMessages

    fun deleteMessages(
        channels: List<String>,
        start: Long? = null,
        end: Long? = null,
    ): DeleteMessages

    fun messageCounts(
        channels: List<String>,
        channelsTimetoken: List<Long>,
    ): MessageCounts

    fun hereNow(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        includeState: Boolean = false,
        includeUUIDs: Boolean = true,
    ): HereNow

    fun whereNow(uuid: String = configuration.userId.value): WhereNow

// TODO bring back when/if all SDKs accept a `uuid` parameter
//    fun setPresenceState(
//        channels: List<String> = listOf(),
//        channelGroups: List<String> = listOf(),
//        state: Any,
//        uuid: String = configuration.userId.value,
//    ): SetState

    fun setPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        state: Any,
    ): SetState

    fun getPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        uuid: String = configuration.userId.value,
    ): GetState

    fun presence(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        connected: Boolean = false,
    )

    fun addMessageAction(
        channel: String,
        messageAction: PNMessageAction,
    ): AddMessageAction

    fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long,
    ): RemoveMessageAction

    fun getMessageActions(
        channel: String,
        page: PNBoundedPage = PNBoundedPage(),
    ): GetMessageActions

    fun addChannelsToChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): AddChannelChannelGroup

    fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup

    fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): RemoveChannelChannelGroup

    fun listAllChannelGroups(): ListAllChannelGroup

    fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup

    fun grantToken(
        ttl: Int,
        meta: CustomObject? = null,
        authorizedUUID: String? = null,
        channels: List<ChannelGrant> = emptyList(),
        channelGroups: List<ChannelGroupGrant> = emptyList(),
        uuids: List<UUIDGrant> = emptyList(),
    ): GrantToken

    fun revokeToken(token: String): RevokeToken

    fun time(): Time

    fun getAllChannelMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
    ): GetAllChannelMetadata

    fun getChannelMetadata(
        channel: String,
        includeCustom: Boolean = false,
    ): GetChannelMetadata

    fun setChannelMetadata(
        channel: String,
        name: String? = null,
        description: String? = null,
        custom: CustomObject? = null,
        includeCustom: Boolean = false,
        type: String? = null,
        status: String? = null,
    ): SetChannelMetadata

    fun removeChannelMetadata(channel: String): RemoveChannelMetadata

    fun getAllUUIDMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
    ): GetAllUUIDMetadata

    fun getUUIDMetadata(
        uuid: String? = null,
        includeCustom: Boolean = false,
    ): GetUUIDMetadata

    fun setUUIDMetadata(
        uuid: String? = null,
        name: String? = null,
        externalId: String? = null,
        profileUrl: String? = null,
        email: String? = null,
        custom: CustomObject? = null,
        includeCustom: Boolean = false,
        type: String? = null,
        status: String? = null,
    ): SetUUIDMetadata

    fun removeUUIDMetadata(uuid: String? = null): RemoveUUIDMetadata

    fun getMemberships(
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null,
        includeType: Boolean = false,
    ): GetMemberships

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
        includeType: Boolean = false,
    ): ManageMemberships

    fun removeMemberships(
        channels: List<String>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null,
        includeType: Boolean = false,
    ): ManageMemberships

//    fun manageMemberships(
//        channelsToSet: List<ChannelMembershipInput>,
//        channelsToRemove: List<String>,
//        uuid: String? = null,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeChannelDetails: PNChannelDetailsLevel? = null,
//    ): ManageMemberships

    fun getChannelMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
        includeType: Boolean = false,
    ): GetChannelMembers

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
        includeType: Boolean = false,
    ): ManageChannelMembers

    fun removeChannelMembers(
        channel: String,
        uuids: List<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
        includeType: Boolean = false,
    ): ManageChannelMembers

//    fun manageChannelMembers(
//        channel: String,
//        uuidsToSet: Collection<MemberInput>,
//        uuidsToRemove: Collection<String>,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeUUIDDetails: PNUUIDDetailsLevel? = null,
//    ): ManageChannelMembers

    //
    fun listFiles(
        channel: String,
        limit: Int? = null,
        next: PNPage.PNNext? = null,
    ): ListFiles

    fun getFileUrl(
        channel: String,
        fileName: String,
        fileId: String,
    ): GetFileUrl

    fun sendFile(
        channel: String,
        fileName: String,
        inputStream: Uploadable,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null,
        cipherKey: String? = null,
    ): SendFile

    fun downloadFile(
        channel: String,
        fileName: String,
        fileId: String,
        cipherKey: String? = null,
    ): DownloadFile

    fun deleteFile(
        channel: String,
        fileName: String,
        fileId: String,
    ): DeleteFile

    fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null,
    ): PublishFileMessage

    fun subscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        withPresence: Boolean = false,
        withTimetoken: Long = 0L,
    )

    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
    )

    fun unsubscribeAll()

    fun setToken(token: String?)

    fun destroy()

    fun channel(name: String): Channel

    fun channelGroup(name: String): ChannelGroup

    fun channelMetadata(id: String): ChannelMetadata

    fun userMetadata(id: String): UserMetadata

    fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet

    fun subscriptionSetOf(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
        options: SubscriptionOptions = EmptyOptions,
    ): SubscriptionSet

    fun parseToken(token: String): PNToken
}
