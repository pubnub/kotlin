package com.pubnub.api.hackathon

import com.pubnub.api.PNConfiguration
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.*
import com.pubnub.api.endpoints.access.Grant
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.endpoints.channel_groups.*
import com.pubnub.api.endpoints.files.*
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
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.managers.*
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.member.PNUUIDWithCustom
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelWithCustom
import java.io.InputStream

interface IPubNub {
    var configuration: PNConfiguration

    val mapper: MapperManager
    val retrofitManager: RetrofitManager
    val telemetryManager: TelemetryManager
    val subscriptionManager: SubscriptionManager
    val tokenManager: TokenManager

    val version: String

    val instanceId: String

    fun baseUrl(): String
    fun requestId(): String
    fun timestamp(): Int

    fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ): RemoteAction<PNPublishResult>

    fun fire(
        channel: String,
        message: Any,
        meta: Any? = null,
        usePost: Boolean = false,
        ttl: Int? = null
    ): RemoteAction<PNPublishResult>

    fun signal(
        channel: String,
        message: Any
    ): Signal

    fun subscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        withPresence: Boolean = false,
        withTimetoken: Long = 0L
    )

    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    )

    fun unsubscribeAll()

    fun getSubscribedChannels(): List<String>

    fun getSubscribedChannelGroups(): List<String>

    fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): AddChannelsToPush

    fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): ListPushProvisions

    fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): RemoveChannelsFromPush

    fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): RemoveAllPushChannelsForDevice

    fun history(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        count: Int = History.MAX_COUNT,
        reverse: Boolean = false,
        includeTimetoken: Boolean = false,
        includeMeta: Boolean = false
    ): History

    fun fetchMessages(
        channels: List<String>,
        maximumPerChannel: Int = 0,
        start: Long? = null,
        end: Long? = null,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false
    ): FetchMessages

    fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage = PNBoundedPage(),
        includeUUID: Boolean = true,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false
    ): FetchMessages


    fun deleteMessages(
        channels: List<String>,
        start: Long? = null,
        end: Long? = null
    ): DeleteMessages


    fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts


    fun hereNow(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        includeState: Boolean = false,
        includeUUIDs: Boolean = true
    ): HereNow

    fun whereNow(uuid: String = configuration.uuid): WhereNow

    fun setPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        state: Any,
        uuid: String = configuration.uuid
    ): SetState

    fun getPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        uuid: String = configuration.uuid
    ): GetState

    fun presence(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        connected: Boolean = false
    )

    fun addMessageAction(channel: String, messageAction: PNMessageAction): AddMessageAction

    fun removeMessageAction(channel: String, messageTimetoken: Long, actionTimetoken: Long): RemoveMessageAction


    fun getMessageActions(
        channel: String,
        page: PNBoundedPage = PNBoundedPage()
    ): GetMessageActions

    fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup

    fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup

    fun removeChannelsFromChannelGroup(channels: List<String>, channelGroup: String): RemoveChannelChannelGroup

    fun listAllChannelGroups(): ListAllChannelGroup

    fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup

    fun grant(
        read: Boolean = false,
        write: Boolean = false,
        manage: Boolean = false,
        delete: Boolean = false,
        ttl: Int = -1,
        authKeys: List<String> = emptyList(),
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ): Grant

    fun grantToken(
        ttl: Int,
        meta: Any? = null,
        authorizedUUID: String? = null,
        channels: List<ChannelGrant> = emptyList(),
        channelGroups: List<ChannelGroupGrant> = emptyList(),
        uuids: List<UUIDGrant> = emptyList()
    ): GrantToken

    fun revokeToken(token: String): RevokeToken

    fun time(): Time

    fun getAllChannelMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ): GetAllChannelMetadata

    fun getChannelMetadata(channel: String, includeCustom: Boolean = false): GetChannelMetadata

    fun setChannelMetadata(
        channel: String,
        name: String? = null,
        description: String? = null,
        custom: Any? = null,
        includeCustom: Boolean = false
    ): SetChannelMetadata

    fun removeChannelMetadata(channel: String): RemoveChannelMetadata

    fun getAllUUIDMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ): GetAllUUIDMetadata

    fun getUUIDMetadata(
        uuid: String? = null,
        includeCustom: Boolean = false
    ): GetUUIDMetadata

    fun setUUIDMetadata(
        uuid: String? = null,
        name: String? = null,
        externalId: String? = null,
        profileUrl: String? = null,
        email: String? = null,
        custom: Any? = null,
        includeCustom: Boolean = false
    ): SetUUIDMetadata

    fun removeUUIDMetadata(uuid: String? = null): RemoveUUIDMetadata

    fun getMemberships(
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): GetMemberships

    fun addMemberships(
        channels: List<PNChannelWithCustom>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships

    fun setMemberships(
        channels: List<PNChannelWithCustom>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships

    fun removeMemberships(
        channels: List<String>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships

    fun manageMemberships(
        channelsToSet: List<PNChannelWithCustom>,
        channelsToRemove: List<String>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships


    fun getMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): GetChannelMembers

    fun getChannelMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): GetChannelMembers

    fun addMembers(
        channel: String,
        uuids: List<PNUUIDWithCustom>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers

    fun setChannelMembers(
        channel: String,
        uuids: List<PNUUIDWithCustom>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers


    fun getMessageActions(
        channel: String,
        start: Long?,
        end: Long?,
        limit: Int?
    ): GetMessageActions

    fun removeMembers(
        channel: String,
        uuids: List<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers

    fun removeChannelMembers(
        channel: String,
        uuids: List<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers


    fun manageChannelMembers(
        channel: String,
        uuidsToSet: Collection<PNUUIDWithCustom>,
        uuidsToRemove: Collection<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers


    fun sendFile(
        channel: String,
        fileName: String,
        inputStream: InputStream,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null,
        cipherKey: String? = null
    ): SendFile


    fun listFiles(
        channel: String,
        limit: Int? = null,
        next: PNPage.PNNext? = null
    ): ListFiles

    fun getFileUrl(
        channel: String,
        fileName: String,
        fileId: String
    ): GetFileUrl

    fun downloadFile(
        channel: String,
        fileName: String,
        fileId: String,
        cipherKey: String? = null
    ): DownloadFile

    fun deleteFile(
        channel: String,
        fileName: String,
        fileId: String
    ): DeleteFile

    fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null
    ): PublishFileMessage


    fun addListener(listener: SubscribeCallback)

    fun removeListener(listener: SubscribeCallback)

    fun decrypt(inputString: String): String

    fun decrypt(inputString: String, cipherKey: String = configuration.cipherKey): String

    fun decryptInputStream(
        inputStream: InputStream,
        cipherKey: String = configuration.cipherKey
    ): InputStream

    fun encrypt(inputString: String, cipherKey: String = configuration.cipherKey): String

    fun encryptInputStream(
        inputStream: InputStream,
        cipherKey: String = configuration.cipherKey
    ): InputStream

    fun reconnect()

    fun disconnect()

    fun destroy()

    fun forceDestroy()
    fun parseToken(token: String): PNToken
    fun setToken(token: String?)
}