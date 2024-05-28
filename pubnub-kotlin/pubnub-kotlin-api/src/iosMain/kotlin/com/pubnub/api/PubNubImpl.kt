package com.pubnub.api

import cocoapods.PubNubSwift.addEventListenerWithListener
import cocoapods.PubNubSwift.subscribeWithChannels
import cocoapods.PubNubSwift.subscribedChannelGroups
import cocoapods.PubNubSwift.subscribedChannels
import cocoapods.PubNubSwift.unsubscribeFrom
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.endpoints.DeleteMessagesImpl
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.endpoints.FetchMessagesImpl
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.MessageCountsImpl
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.TimeImpl
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.api.endpoints.files.PublishFileMessage
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
import com.pubnub.api.endpoints.pubsub.PublishImpl
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.endpoints.pubsub.SignalImpl
import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.api.endpoints.push.AddChannelsToPushImpl
import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.api.endpoints.push.ListPushProvisionsImpl
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDeviceImpl
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.endpoints.push.RemoveChannelsFromPushImpl
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
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
import com.pubnub.kmp.CustomObject
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class PubNubImpl(override val configuration: PNConfiguration) : PubNub {
    private val pubNubObjC = cocoapods.PubNubSwift.PubNubObjC(
        user = configuration.userId.value,
        subKey = configuration.subscribeKey,
        pubKey = configuration.publishKey
    )

    override fun addListener(listener: EventListener) {
        pubNubObjC.addEventListenerWithListener(listener = listener.underlying)
    }

    override fun addListener(listener: StatusListener) {
        TODO("Not yet implemented")
    }

    override fun removeListener(listener: Listener) {
        TODO("Not yet implemented")
    }

    override fun removeAllListeners() {
        TODO("Not yet implemented")
    }

    // TODO: replicate and usePost parameters are not present in Swift SDK
    override fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?
    ): Publish {
        return PublishImpl(
            pubnub = pubNubObjC,
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = shouldStore,
            ttl = ttl
        )
    }

    // TODO: usePost parameter is not present in Swift SDK
    override fun fire(channel: String, message: Any, meta: Any?, usePost: Boolean, ttl: Int?): Publish {
        return PublishImpl(
            pubnub = pubNubObjC,
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = false,
            ttl = 0
        )
    }

    override fun signal(channel: String, message: Any): Signal {
        return SignalImpl(pubnub = pubNubObjC, channel = channel, message = message)
    }

    override fun getSubscribedChannels(): List<String> {
        return pubNubObjC.subscribedChannels() as List<String>
    }

    override fun getSubscribedChannelGroups(): List<String> {
        return pubNubObjC.subscribedChannelGroups() as List<String>
    }


    // TODO: Missing pushType (PushService like APNS, GCM, etc) parameter
    // TODO: Why do we need topic parameter here?
    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): AddChannelsToPush {
        return AddChannelsToPushImpl(
            pubnub = pubNubObjC,
            channels = channels,
            deviceId = deviceId
        )
    }

    // TODO: topic and environment parameters are not present in Swift SDK
    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): ListPushProvisions {
        return ListPushProvisionsImpl(pubnub = pubNubObjC, deviceId = deviceId, pushType = pushType)
    }

    // TODO: topic and environment parameters are not present in Swift SDK
    override fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): RemoveChannelsFromPush {
        return RemoveChannelsFromPushImpl(
            pubnub = pubNubObjC,
            channels = channels,
            deviceId = deviceId,
            pushType = pushType
        )
    }

    override fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): RemoveAllPushChannelsForDevice {
        return RemoveAllPushChannelsForDeviceImpl(
            pubnub = pubNubObjC,
            deviceId = deviceId,
            pushType = pushType
        )
    }

    override fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean
    ): FetchMessages {
        return FetchMessagesImpl(
            pubnub = pubNubObjC,
            channels = channels,
            page = page,
            includeUUID = includeUUID,
            includeMeta = includeMeta,
            includeMessageActions = includeMessageActions,
            includeMessageType = includeMessageType
        )
    }

    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): DeleteMessages {
        return DeleteMessagesImpl(
            pubnub = pubNubObjC,
            channels = channels,
            start = start,
            end = end
        )
    }

    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts {
        return MessageCountsImpl(
            pubnub = pubNubObjC,
            channels = channels,
            channelsTimetoken = channelsTimetoken
        )
    }

    override fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean
    ): HereNow {
        TODO("Not yet implemented")
    }

    override fun whereNow(uuid: String): WhereNow {
        TODO("Not yet implemented")
    }

    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
    ): SetState {
        TODO("Not yet implemented")
    }

    override fun getPresenceState(channels: List<String>, channelGroups: List<String>, uuid: String): GetState {
        TODO("Not yet implemented")
    }

    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addMessageAction(channel: String, messageAction: PNMessageAction): AddMessageAction {
        TODO("Not yet implemented")
    }

    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): RemoveMessageAction {
        TODO("Not yet implemented")
    }

    override fun getMessageActions(channel: String, page: PNBoundedPage): GetMessageActions {
        TODO("Not yet implemented")
    }

    override fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup {
        TODO("Not yet implemented")
    }

    override fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        TODO("Not yet implemented")
    }

    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): RemoveChannelChannelGroup {
        TODO("Not yet implemented")
    }

    override fun listAllChannelGroups(): ListAllChannelGroup {
        TODO("Not yet implemented")
    }

    override fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        TODO("Not yet implemented")
    }

    override fun grantToken(
        ttl: Int,
        meta: CustomObject?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>
    ): GrantToken {
        TODO("Not yet implemented")
    }

    override fun revokeToken(token: String): RevokeToken {
        TODO("Not yet implemented")
    }

    override fun time(): Time {
        return TimeImpl(pubnub = pubNubObjC)
    }

    override fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): GetAllChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun getChannelMetadata(channel: String, includeCustom: Boolean): GetChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: CustomObject?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): SetChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): GetAllUUIDMetadata {
        TODO("Not yet implemented")
    }

    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): GetUUIDMetadata {
        TODO("Not yet implemented")
    }

    override fun setUUIDMetadata(
        uuid: String?,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: CustomObject?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): SetUUIDMetadata {
        TODO("Not yet implemented")
    }

    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
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
    ): GetMemberships {
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
    ): ManageMemberships {
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
    ): ManageMemberships {
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
    ): ManageMemberships {
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
    ): GetChannelMembers {
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
    ): ManageChannelMembers {
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
    ): ManageChannelMembers {
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
    ): ManageChannelMembers {
        TODO("Not yet implemented")
    }

    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): ListFiles {
        TODO("Not yet implemented")
    }

    override fun getFileUrl(channel: String, fileName: String, fileId: String): GetFileUrl {
        TODO("Not yet implemented")
    }

    override fun deleteFile(channel: String, fileName: String, fileId: String): DeleteFile {
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
    ): PublishFileMessage {
        TODO("Not yet implemented")
    }

    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        pubNubObjC.subscribeWithChannels(
            channels = channels,
            channelGroups = channelGroups,
            withPresence = withPresence,
            timetoken = withTimetoken.toULong()
        )
    }

    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
        pubNubObjC.unsubscribeFrom(channels = channels, channelGroups = channelGroups)
    }

    override fun setToken(token: String?) {
        TODO("Not yet implemented")
    }

    override fun destroy() {
//        TODO("Not yet implemented")
    }

    override fun unsubscribeAll() {
//        TODO("Not yet implemented")
    }
}