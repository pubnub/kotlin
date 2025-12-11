package com.pubnub.api

import cocoapods.PubNubSwift.KMPLogLevel
import cocoapods.PubNubSwift.KMPPAMPermission
import cocoapods.PubNubSwift.KMPPAMTokenResource
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.KMPSubscription
import cocoapods.PubNubSwift.KMPSubscriptionSet
import cocoapods.PubNubSwift.addEventListenerWithListener
import cocoapods.PubNubSwift.addStatusListenerWithListener
import cocoapods.PubNubSwift.channelGroupWith
import cocoapods.PubNubSwift.channelMetadataWith
import cocoapods.PubNubSwift.channelWith
import cocoapods.PubNubSwift.disconnect
import cocoapods.PubNubSwift.getToken
import cocoapods.PubNubSwift.logLevel
import cocoapods.PubNubSwift.parseWithToken
import cocoapods.PubNubSwift.reconnectWithTimetoken
import cocoapods.PubNubSwift.removeAllListeners
import cocoapods.PubNubSwift.removeEventListenerWithListener
import cocoapods.PubNubSwift.removeStatusListenerWithListener
import cocoapods.PubNubSwift.setWithToken
import cocoapods.PubNubSwift.subscribeWithChannels
import cocoapods.PubNubSwift.subscribedChannelGroups
import cocoapods.PubNubSwift.subscribedChannels
import cocoapods.PubNubSwift.unsubscribeAll
import cocoapods.PubNubSwift.unsubscribeFrom
import cocoapods.PubNubSwift.userMetadataWith
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.endpoints.FetchMessagesImpl
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.MessageCountsImpl
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.TimeImpl
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroupImpl
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroupImpl
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroupImpl
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroupImpl
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroupImpl
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.DeleteFileImpl
import com.pubnub.api.endpoints.files.DownloadFile
import com.pubnub.api.endpoints.files.DownloadFileImpl
import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.api.endpoints.files.GetFileUrlImpl
import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.api.endpoints.files.ListFilesImpl
import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.endpoints.files.PublishFileMessageImpl
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.files.SendFileImpl
import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.api.endpoints.message_actions.AddMessageActionImpl
import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.endpoints.message_actions.GetMessageActionsImpl
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.endpoints.message_actions.RemoveMessageActionImpl
import com.pubnub.api.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.api.endpoints.objects.channel.GetAllChannelMetadataImpl
import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.api.endpoints.objects.channel.GetChannelMetadataImpl
import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadata
import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadataImpl
import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.api.endpoints.objects.channel.SetChannelMetadataImpl
import com.pubnub.api.endpoints.objects.member.GetChannelMembers
import com.pubnub.api.endpoints.objects.member.GetChannelMembersImpl
import com.pubnub.api.endpoints.objects.member.ManageChannelMembers
import com.pubnub.api.endpoints.objects.member.RemoveChannelMembersImpl
import com.pubnub.api.endpoints.objects.member.SetChannelMembersImpl
import com.pubnub.api.endpoints.objects.membership.AddMembershipsImpl
import com.pubnub.api.endpoints.objects.membership.GetMemberships
import com.pubnub.api.endpoints.objects.membership.GetMembershipsImpl
import com.pubnub.api.endpoints.objects.membership.ManageMemberships
import com.pubnub.api.endpoints.objects.membership.RemoveMembershipsImpl
import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadataImpl
import com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadataImpl
import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadataImpl
import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadataImpl
import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.api.endpoints.presence.GetStateImpl
import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.endpoints.presence.HereNowImpl
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.endpoints.presence.SetStateImpl
import com.pubnub.api.endpoints.presence.WhereNow
import com.pubnub.api.endpoints.presence.WhereNowImpl
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
import com.pubnub.api.enums.LogLevel
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
import com.pubnub.api.v2.createPNConfiguration
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.entities.ChannelGroupImpl
import com.pubnub.internal.entities.ChannelImpl
import com.pubnub.internal.entities.ChannelMetadataImpl
import com.pubnub.internal.entities.UserMetadataImpl
import com.pubnub.internal.subscription.SubscriptionImpl
import com.pubnub.internal.subscription.SubscriptionSetImpl
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.Uploadable
import com.pubnub.kmp.safeCast
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class PubNubImpl(private val pubNubObjC: KMPPubNub) : PubNub {
    constructor(configuration: PNConfiguration) : this(
        KMPPubNub(
            user = configuration.userId.value,
            subKey = configuration.subscribeKey,
            pubKey = configuration.publishKey,
            logLevel = KMPLogLevel(configuration.logLevel.levels.fold(0u) { acc, level -> acc or level.value })
        )
    )

    companion object {
        fun create(kmpPubNub: Any): PubNubImpl {
            return PubNubImpl(kmpPubNub as KMPPubNub)
        }
    }

    override val configuration: PNConfiguration = createPNConfiguration(
        userId = UserId(pubNubObjC.configObjC().userId()),
        subscribeKey = pubNubObjC.configObjC().subscribeKey(),
        publishKey = pubNubObjC.configObjC().publishKey().orEmpty(),
        logLevel = mapToLogLevel(pubNubObjC.logLevel())
    )

    private fun mapToLogLevel(level: KMPLogLevel): LogLevel {
        val currentMask = level.rawValue()
        val levels = LogLevel.Level.entries.filter { l -> (currentMask and l.value) != 0u }.toSet()

        return LogLevel(levels)
    }

    override fun addListener(listener: EventListener) {
        pubNubObjC.addEventListenerWithListener(listener = listener.underlying)
    }

    override fun addListener(listener: StatusListener) {
        pubNubObjC.addStatusListenerWithListener(listener = listener.underlying)
    }

    override fun removeListener(listener: Listener) {
        when (listener) {
            is EventListener -> pubNubObjC.removeEventListenerWithListener(listener.underlying)
            is StatusListener -> pubNubObjC.removeStatusListenerWithListener(listener.underlying)
        }
    }

    override fun removeAllListeners() {
        pubNubObjC.removeAllListeners()
    }

    // TODO: replicate is not present in Swift SDK
    override fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?,
        customMessageType: String?,
    ): Publish {
        return PublishImpl(
            pubnub = pubNubObjC,
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = shouldStore,
            usePost = usePost,
            ttl = ttl,
            customMessageType = customMessageType
        )
    }

    override fun fire(channel: String, message: Any, meta: Any?, usePost: Boolean): Publish {
        return PublishImpl(
            pubnub = pubNubObjC,
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = false,
            usePost = usePost,
            ttl = 0,
            customMessageType = null
        )
    }

    override fun signal(channel: String, message: Any, customMessageType: String?): Signal {
        return SignalImpl(
            pubnub = pubNubObjC,
            channel = channel,
            message = message,
            customMessageType = customMessageType
        )
    }

    override fun getSubscribedChannels(): List<String> {
        return pubNubObjC.subscribedChannels().filterIsInstance<String>()
    }

    override fun getSubscribedChannelGroups(): List<String> {
        return pubNubObjC.subscribedChannelGroups().filterIsInstance<String>()
    }

    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): AddChannelsToPush {
        return AddChannelsToPushImpl(
            pushType = pushType,
            pubnub = pubNubObjC,
            channels = channels,
            deviceId = deviceId,
            topic = topic,
            environment = environment
        )
    }

    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): ListPushProvisions {
        return ListPushProvisionsImpl(
            pubnub = pubNubObjC,
            deviceId = deviceId,
            pushType = pushType,
            topic = topic,
            environment = environment
        )
    }

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
            pushType = pushType,
            topic = topic,
            environment = environment
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
            pushType = pushType,
            topic = topic,
            environment = environment
        )
    }

    override fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean,
        includeCustomMessageType: Boolean
    ): FetchMessages {
        return FetchMessagesImpl(
            pubnub = pubNubObjC,
            channels = channels,
            page = page,
            includeUUID = includeUUID,
            includeMeta = includeMeta,
            includeMessageActions = includeMessageActions,
            includeMessageType = includeMessageType,
            includeCustomMessageType = includeCustomMessageType
        )
    }

    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): DeleteMessages {
        return com.pubnub.api.endpoints.DeleteMessagesImpl(
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
        includeUUIDs: Boolean,
        limit: Int,
        offset: Int?
    ): HereNow {
        return HereNowImpl(
            pubnub = pubNubObjC,
            channels = channels,
            channelGroups = channelGroups,
            includeState = includeState,
            includeUUIDs = includeUUIDs,
            limit = limit,
            offset = offset
        )
    }

    override fun whereNow(uuid: String): WhereNow {
        return WhereNowImpl(
            pubnub = pubNubObjC,
            uuid = uuid
        )
    }

    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
    ): SetState {
        return SetStateImpl(
            pubnub = pubNubObjC,
            channels = channels,
            channelGroups = channelGroups,
            state = state
        )
    }

    override fun getPresenceState(channels: List<String>, channelGroups: List<String>, uuid: String): GetState {
        return GetStateImpl(
            pubnub = pubNubObjC,
            channels = channels,
            channelGroups = channelGroups,
            uuid = uuid
        )
    }

    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addMessageAction(channel: String, messageAction: PNMessageAction): AddMessageAction {
        return AddMessageActionImpl(
            pubnub = pubNubObjC,
            channel = channel,
            actionType = messageAction.type,
            actionValue = messageAction.value,
            messageTimetoken = messageAction.messageTimetoken
        )
    }

    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): RemoveMessageAction {
        return RemoveMessageActionImpl(
            pubnub = pubNubObjC,
            channel = channel,
            messageTimetoken = messageTimetoken,
            actionTimetoken = actionTimetoken
        )
    }

    override fun getMessageActions(channel: String, page: PNBoundedPage): GetMessageActions {
        return GetMessageActionsImpl(
            pubnub = pubNubObjC,
            channel = channel,
            page = page
        )
    }

    override fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup {
        return AddChannelChannelGroupImpl(
            pubnub = pubNubObjC,
            channels = channels,
            channelGroup = channelGroup
        )
    }

    override fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        return AllChannelsChannelGroupImpl(
            pubnub = pubNubObjC,
            channelGroup = channelGroup
        )
    }

    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): RemoveChannelChannelGroup {
        return RemoveChannelChannelGroupImpl(
            pubnub = pubNubObjC,
            channels = channels,
            channelGroup = channelGroup
        )
    }

    override fun listAllChannelGroups(): ListAllChannelGroup {
        return ListAllChannelGroupImpl(pubnub = pubNubObjC)
    }

    override fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        return DeleteChannelGroupImpl(
            pubnub = pubNubObjC,
            channelGroup = channelGroup
        )
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
        return GetAllChannelMetadataImpl(
            pubnub = pubNubObjC,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom
        )
    }

    override fun getChannelMetadata(channel: String, includeCustom: Boolean): GetChannelMetadata {
        return GetChannelMetadataImpl(
            pubnub = pubNubObjC,
            channelId = channel,
            includeCustom = includeCustom
        )
    }

    override fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: CustomObject?,
        includeCustom: Boolean,
        type: String?,
        status: String?,
        ifMatchesEtag: String?,
    ): SetChannelMetadata {
        return SetChannelMetadataImpl(
            pubnub = pubNubObjC,
            metadataId = channel,
            name = name,
            description = description,
            custom = custom,
            includeCustom = includeCustom,
            type = type,
            status = status,
            ifMatchesEtag = ifMatchesEtag,
        )
    }

    override fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        return RemoveChannelMetadataImpl(
            pubnub = pubNubObjC,
            metadataId = channel
        )
    }

    override fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): GetAllUUIDMetadata {
        return GetAllUUIDMetadataImpl(
            pubnub = pubNubObjC,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom
        )
    }

    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): GetUUIDMetadata {
        return GetUUIDMetadataImpl(
            pubnub = pubNubObjC,
            metadataId = uuid,
            includeCustom = includeCustom
        )
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
        status: String?,
        ifMatchesEtag: String?,
    ): SetUUIDMetadata {
        return SetUUIDMetadataImpl(
            pubnub = pubNubObjC,
            metadataId = uuid,
            name = name,
            externalId = externalId,
            profileUrl = profileUrl,
            email = email,
            custom = custom,
            includeCustom = includeCustom,
            type = type,
            status = status,
            ifMatchesEtag = ifMatchesEtag,
        )
    }

    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
        return RemoveUUIDMetadataImpl(
            pubnub = pubNubObjC,
            metadataId = uuid
        )
    }

    // deprecated
    override fun getMemberships(
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean,
    ): GetMemberships {
        return getMemberships(
            userId = uuid,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            include = MembershipInclude(
                includeTotalCount = includeCount,
                includeCustom = includeCustom,
                includeType = includeType,
                includeChannel = includeChannelDetails == PNChannelDetailsLevel.CHANNEL || includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
                includeChannelCustom = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
            )
        )
    }

    override fun getMemberships(
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude
    ): GetMemberships {
        return GetMembershipsImpl(
            pubnub = pubNubObjC,
            userId = userId,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeFields = include
        )
    }

    // deprecated
    override fun setMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean,
    ): ManageMemberships {
        return setMemberships(
            channels = channels,
            userId = uuid,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            include = MembershipInclude(
                includeTotalCount = includeCount,
                includeCustom = includeCustom,
                includeChannel = includeChannelDetails == PNChannelDetailsLevel.CHANNEL || includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
                includeChannelType = includeType,
                includeChannelCustom = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
            )
        )
    }

    override fun setMemberships(
        channels: List<ChannelMembershipInput>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude
    ): ManageMemberships {
        return AddMembershipsImpl(
            pubnub = pubNubObjC,
            channels = channels,
            userId = userId,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeFields = include
        )
    }

    // deprecated
    override fun removeMemberships(
        channels: List<String>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean,
    ): ManageMemberships {
        return removeMemberships(
            channels = channels,
            userId = uuid,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            include = MembershipInclude(
                includeTotalCount = includeCount,
                includeCustom = includeCustom,
                includeChannel = includeChannelDetails == PNChannelDetailsLevel.CHANNEL || includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
                includeChannelType = includeType,
                includeChannelCustom = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
            )
        )
    }

    override fun removeMemberships(
        channels: List<String>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude,
    ): ManageMemberships {
        return RemoveMembershipsImpl(
            pubnub = pubNubObjC,
            channels = channels,
            userId = userId,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeFields = include
        )
    }

    // deprecated
    override fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
        includeType: Boolean,
    ): GetChannelMembers {
        return getChannelMembers(
            channel = channel,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            include = MemberInclude(
                includeTotalCount = includeCount,
                includeCustom = includeCustom,
                includeType = includeType,
                includeUser = includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
                includeUserCustom = includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            )
        )
    }

    override fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): GetChannelMembers {
        return GetChannelMembersImpl(
            pubnub = pubNubObjC,
            channelId = channel,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeFields = include
        )
    }

    // deprecated
    override fun setChannelMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
        includeType: Boolean,
    ): ManageChannelMembers {
        return setChannelMembers(
            channel = channel,
            users = uuids,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            include = MemberInclude(
                includeTotalCount = includeCount,
                includeCustom = includeCustom,
                includeType = includeType,
                includeStatus = true,
                includeUser = includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
                includeUserCustom = includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            )
        )
    }

    override fun setChannelMembers(
        channel: String,
        users: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): ManageChannelMembers {
        return SetChannelMembersImpl(
            pubnub = pubNubObjC,
            channelId = channel,
            users = users,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeFields = include
        )
    }

    // deprecated
    override fun removeChannelMembers(
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
    ): ManageChannelMembers {
        return removeChannelMembers(
            channel = channel,
            userIds = uuids,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            include = MemberInclude(
                includeTotalCount = includeCount,
                includeCustom = includeCustom,
                includeType = includeType,
                includeUser = includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
                includeUserCustom = includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            )
        )
    }

    override fun removeChannelMembers(
        channel: String,
        userIds: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): ManageChannelMembers {
        return RemoveChannelMembersImpl(
            pubnub = pubNubObjC,
            channel = channel,
            userIds = userIds,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeFields = include
        )
    }

    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): ListFiles {
        return ListFilesImpl(
            pubnub = pubNubObjC,
            channel = channel,
            limit = limit,
            next = next
        )
    }

    override fun getFileUrl(channel: String, fileName: String, fileId: String): GetFileUrl {
        return GetFileUrlImpl(
            pubnub = pubNubObjC,
            channel = channel,
            fileName = fileName,
            fileId = fileId
        )
    }

    override fun deleteFile(channel: String, fileName: String, fileId: String): DeleteFile {
        return DeleteFileImpl(
            pubnub = pubNubObjC,
            channel = channel,
            fileName = fileName,
            fileId = fileId
        )
    }

    override fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        customMessageType: String?
    ): PublishFileMessage {
        return PublishFileMessageImpl(
            pubnub = pubNubObjC,
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            message = message,
            meta = meta,
            ttl = ttl,
            shouldStore = shouldStore,
            customMessageType = customMessageType
        )
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
        pubNubObjC.unsubscribeFrom(
            channels = channels,
            channelGroups = channelGroups
        )
    }

    // TODO: Why token is optional? What's the desired behavior for a null value?
    override fun setToken(token: String?) {
        token?.let {
            pubNubObjC.setWithToken(it)
        }
    }

    override fun getToken(): String? {
        return pubNubObjC.getToken()
    }

    override fun destroy() {
        pubNubObjC.disconnect()
    }

    override fun channel(name: String): Channel {
        return ChannelImpl(channel = pubNubObjC.channelWith(name = name))
    }

    override fun channelGroup(name: String): ChannelGroup {
        return ChannelGroupImpl(channelGroup = pubNubObjC.channelGroupWith(name = name))
    }

    override fun channelMetadata(id: String): ChannelMetadata {
        return ChannelMetadataImpl(channelMetadata = pubNubObjC.channelMetadataWith(id = id))
    }

    override fun userMetadata(id: String): UserMetadata {
        return UserMetadataImpl(userMetadata = pubNubObjC.userMetadataWith(id = id))
    }

    override fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet {
        return SubscriptionSetImpl(
            KMPSubscriptionSet(subscriptions.filterIsInstance<SubscriptionImpl>().map { it.objCSubscription })
        )
    }

    override fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions
    ): SubscriptionSet {
        val channelSubscriptions = channels.map { pubNubObjC.channelWith(it) }.map { entity -> KMPSubscription(entity) }
        val channelGroupSubscriptions = channelGroups.map { pubNubObjC.channelGroupWith(it) }.map {
                entity ->
            KMPSubscription(entity)
        }

        return SubscriptionSetImpl(
            KMPSubscriptionSet(channelGroupSubscriptions + channelSubscriptions)
        )
    }

    override fun parseToken(token: String): PNToken {
        return pubNubObjC.parseWithToken(token)?.let {
            PNToken(
                version = it.version().intValue,
                timestamp = it.timestamp().longValue(),
                authorizedUUID = it.authorizedUUID(),
                resources = mapPAMTokenResources(it.resources()),
                patterns = mapPAMTokenResources(it.patterns()),
                meta = it.meta().asMap()
            )
        } ?: PNToken(
            resources = PNToken.PNTokenResources(),
            patterns = PNToken.PNTokenResources()
        )
    }

    private fun mapPAMTokenResources(from: KMPPAMTokenResource): PNToken.PNTokenResources {
        val channels = from.channels().safeCast<String, KMPPAMPermission>().mapValues {
            mapPAMTokenResourcePermission(it.value)
        }
        val channelGroups = from.channelGroups().safeCast<String, KMPPAMPermission>().mapValues {
            mapPAMTokenResourcePermission(it.value)
        }
        val uuids = from.uuids().safeCast<String, KMPPAMPermission>().mapValues {
            mapPAMTokenResourcePermission(it.value)
        }

        return PNToken.PNTokenResources(
            channels = channels,
            channelGroups = channelGroups,
            uuids = uuids
        )
    }

    private fun mapPAMTokenResourcePermission(from: KMPPAMPermission): PNToken.PNResourcePermissions {
        return PNToken.PNResourcePermissions(
            read = from.read(),
            write = from.write(),
            manage = from.manage(),
            delete = from.delete(),
            get = from.get(),
            update = from.update(),
            join = from.join()
        )
    }

    override fun unsubscribeAll() {
        pubNubObjC.unsubscribeAll()
    }

    // TODO: cipherKey is used from PubNubConfiguration in Swift SDK
    override fun sendFile(
        channel: String,
        fileName: String,
        inputStream: Uploadable,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?,
        customMessageType: String?
    ): SendFile {
        return SendFileImpl(
            pubnub = pubNubObjC,
            channel = channel,
            fileName = fileName,
            inputStream = inputStream,
            message = message,
            meta = meta,
            ttl = ttl,
            shouldStore = shouldStore,
            customMessageType = customMessageType
        )
    }

    override fun downloadFile(channel: String, fileName: String, fileId: String, cipherKey: String?): DownloadFile {
        return DownloadFileImpl(
            pubnub = pubNubObjC,
            channel = channel,
            fileName = fileName,
            fileId = fileId
        )
    }

    override fun disconnect() {
        pubNubObjC.disconnect()
    }

    override fun reconnect(timetoken: Long) {
        pubNubObjC.reconnectWithTimetoken(platform.Foundation.NSNumber(unsignedLongLong = timetoken.toULong()))
    }
}
