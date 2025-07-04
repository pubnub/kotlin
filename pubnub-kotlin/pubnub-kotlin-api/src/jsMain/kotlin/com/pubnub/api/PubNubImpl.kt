package com.pubnub.api

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
import com.pubnub.api.endpoints.access.GrantTokenImpl
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.endpoints.access.RevokeTokenImpl
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
import com.pubnub.api.endpoints.message_actions.GetMessageActionImpl
import com.pubnub.api.endpoints.message_actions.GetMessageActions
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
import com.pubnub.api.endpoints.objects.membership.GetMemberships
import com.pubnub.api.endpoints.objects.membership.GetMembershipsImpl
import com.pubnub.api.endpoints.objects.membership.ManageMemberships
import com.pubnub.api.endpoints.objects.membership.RemoveMembershipsImpl
import com.pubnub.api.endpoints.objects.membership.SetMembershipsImpl
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
import com.pubnub.api.endpoints.pubsub.FireImpl
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
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNAbstractGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.SortField
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.utils.PatchValue
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.createPNConfiguration
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.v2.entities.ChannelGroupImpl
import com.pubnub.internal.v2.entities.ChannelImpl
import com.pubnub.internal.v2.subscriptions.SubscriptionSetImpl
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.JsMap
import com.pubnub.kmp.Uploadable
import com.pubnub.kmp.createJsObject
import com.pubnub.kmp.toJsMap
import com.pubnub.kmp.toMap
import kotlin.js.json
import PubNub as PubNubJs

class PubNubImpl(val jsPubNub: PubNubJs) : PubNub {
    constructor(configuration: PNConfiguration) : this(PubNubJs(configuration.toJs()))

    override val configuration: PNConfiguration
        get() = createPNConfiguration( // todo test this!
            UserId(jsPubNub.getUUID()),
            jsPubNub.asDynamic().configuration.subscribeKey,
            jsPubNub.asDynamic().configuration.publishKey,
            jsPubNub.asDynamic().configuration.secretKey,
            jsPubNub.asDynamic().configuration.logVerbosity,
            authToken = jsPubNub.asDynamic().configuration.authToken
        )

    override fun addListener(listener: EventListener) {
        jsPubNub.addListener(
            listener.asDynamic().unsafeCast<PubNubJs.ListenerParameters>()
        ) // todo figure out a better way (similar to DelegatingEventListener in JVM)
    }

    override fun addListener(listener: StatusListener) {
        jsPubNub.addListener(
            listener.asDynamic().unsafeCast<PubNubJs.StatusListenerParameters>()
        ) // todo figure out a better way (similar to DelegatingEventListener in JVM)
    }

    override fun removeListener(listener: Listener) {
        jsPubNub.removeListener(listener)
    }

    override fun removeAllListeners() {
        TODO("Not yet implemented")
    }

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
            jsPubNub,
            createJsObject {
                this.message = message.adjustCollectionTypes()
                this.channel = channel
                shouldStore?.let { this.storeInHistory = it }
                this.meta = meta?.adjustCollectionTypes()
                this.sendByPost = usePost
                this.ttl = ttl
                this.customMessageType = customMessageType
            }
        )
    }

    override fun fire(channel: String, message: Any, meta: Any?, usePost: Boolean): Publish {
        return FireImpl(
            jsPubNub,
            createJsObject {
                this.message = message.adjustCollectionTypes()
                this.channel = channel
                this.meta = meta?.adjustCollectionTypes()
                this.sendByPost = usePost
            }
        )
    }

    override fun signal(channel: String, message: Any, customMessageType: String?): Signal {
        return SignalImpl(
            jsPubNub,
            createJsObject {
                this.message = message.adjustCollectionTypes()
                this.channel = channel
                this.customMessageType = customMessageType
            }
        )
    }

    override fun getSubscribedChannels(): List<String> = jsPubNub.getSubscribedChannels().toList()

    override fun getSubscribedChannelGroups(): List<String> = jsPubNub.getSubscribedChannelGroups().toList()

    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): AddChannelsToPush {
        return AddChannelsToPushImpl(
            jsPubNub,
            createJsObject {
                this.pushGateway = pushType.toParamString()
                this.channels = channels.toTypedArray()
                this.device = deviceId
                this.topic = topic
                this.environment = environment.toParamString()
            }
        )
    }

    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): ListPushProvisions {
        return ListPushProvisionsImpl(
            jsPubNub,
            createJsObject {
                this.pushGateway = pushType.toParamString()
                this.device = deviceId
                this.topic = topic
                this.environment = environment.toParamString()
            }
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
            jsPubNub,
            createJsObject {
                this.pushGateway = pushType.toParamString()
                this.channels = channels.toTypedArray()
                this.device = deviceId
                this.topic = topic
                this.environment = environment.toParamString()
            }
        )
    }

    override fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): RemoveAllPushChannelsForDevice {
        return RemoveAllPushChannelsForDeviceImpl(
            jsPubNub,
            createJsObject {
                this.pushGateway = pushType.toParamString()
                this.device = deviceId
                this.topic = topic
                this.environment = environment.toParamString()
            }
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
            jsPubNub,
            createJsObject {
                this.channels = channels.toTypedArray()
                this.start = page.start?.toString()
                this.end = page.end?.toString()
                this.count = page.limit
                this.includeUUID = includeUUID
                this.includeMeta = includeMeta
                this.withMessageActions = includeMessageActions
                this.includeMessageActions = includeMessageActions
                this.includeMessageType = includeMessageType
                this.stringifiedTimeToken = true
                this.includeCustomMessageType = includeCustomMessageType
            }
        )
    }

    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): DeleteMessages {
        return DeleteMessagesImpl(
            jsPubNub,
            createJsObject {
                this.channel = channels.first() // channels.toTypedArray() todo JS doesn't accept multiple channels here!
                this.start = start?.toString()
                this.end = end?.toString()
            }
        )
    }

    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts {
        return MessageCountsImpl(
            jsPubNub,
            createJsObject {
                this.channels = channels.toTypedArray()
                this.channelTimetokens = channelsTimetoken.map { it.toString() }.toTypedArray()
            }
        )
    }

    override fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean
    ): HereNow {
        return HereNowImpl(
            jsPubNub,
            createJsObject {
                this.channels = channels.toTypedArray()
                this.channelGroups = channelGroups.toTypedArray()
                this.includeState = includeState
                this.includeUUIDs = includeUUIDs
            }
        )
    }

    override fun whereNow(uuid: String): WhereNow {
        return WhereNowImpl(
            jsPubNub,
            createJsObject {
                this.uuid = uuid
            }
        )
    }

    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
    ): SetState {
        return SetStateImpl(
            jsPubNub,
            createJsObject {
                this.state = state
                this.channels = channels.toTypedArray()
                this.channelGroups = channelGroups.toTypedArray()
            }
        )
    }

    override fun getPresenceState(channels: List<String>, channelGroups: List<String>, uuid: String): GetState {
        return GetStateImpl(
            jsPubNub,
            createJsObject {
                this.channels = channels.toTypedArray()
                this.channelGroups = channelGroups.toTypedArray()
                this.uuid = uuid
            }
        )
    }

    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addMessageAction(channel: String, messageAction: PNMessageAction): AddMessageAction {
        return AddMessageActionImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.messageTimetoken = messageAction.messageTimetoken.toString()
                this.action = createJsObject {
                    this.type = messageAction.type
                    this.value = messageAction.value
                }
            }
        )
    }

    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): RemoveMessageAction {
        return RemoveMessageActionImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.messageTimetoken = messageTimetoken.toString()
                this.actionTimetoken = actionTimetoken.toString()
            }
        )
    }

    override fun getMessageActions(channel: String, page: PNBoundedPage): GetMessageActions {
        return GetMessageActionImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.start = page.start?.toString()
                this.end = page.end?.toString()
                this.limit = page.limit
            }
        )
    }

    override fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup {
        return AddChannelChannelGroupImpl(
            jsPubNub,
            createJsObject {
                this.channels = channels.toTypedArray()
                this.channelGroup = channelGroup
            }
        )
    }

    override fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        return AllChannelsChannelGroupImpl(
            jsPubNub,
            createJsObject {
                this.channelGroup = channelGroup
            }
        )
    }

    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): RemoveChannelChannelGroup {
        return RemoveChannelChannelGroupImpl(
            jsPubNub,
            createJsObject {
                this.channels = channels.toTypedArray()
                this.channelGroup = channelGroup
            }
        )
    }

    override fun listAllChannelGroups(): ListAllChannelGroup {
        return ListAllChannelGroupImpl(jsPubNub)
    }

    override fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        return DeleteChannelGroupImpl(
            jsPubNub,
            createJsObject {
                this.channelGroup = channelGroup
            }
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
        return GrantTokenImpl(
            jsPubNub,
            createJsObject {
                this.meta = meta?.let { metaNotNull ->
                    json(*metaNotNull.entries.map { Pair(it.key, it.value) }.toTypedArray())
                }
                this.ttl = ttl
                this.authorized_uuid = authorizedUUID
                this.resources = createJsObject<PubNubJs.PatternsOrResources> {
                    this.channels = getGrantTokenPermissions<PNResourceGrant>(channels)
                    this.groups = getGrantTokenPermissions<PNResourceGrant>(channelGroups)
                    this.uuids = getGrantTokenPermissions<PNResourceGrant>(uuids)
                }
                this.patterns = createJsObject<PubNubJs.PatternsOrResources> {
                    this.channels = getGrantTokenPermissions<PNPatternGrant>(channels)
                    this.groups = getGrantTokenPermissions<PNPatternGrant>(channelGroups)
                    this.uuids = getGrantTokenPermissions<PNPatternGrant>(uuids)
                }
            }
        )
    }

    override fun revokeToken(token: String): RevokeToken {
        return RevokeTokenImpl(jsPubNub, token)
    }

    override fun time(): Time {
        return TimeImpl(jsPubNub)
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
            jsPubNub,
            createJsObject {
                this.page = page.toMetadataPage()
                this.filter = filter
                this.limit = limit
                this.sort = sort.toJsMap()
                this.include = createJsObject<PubNubJs.MetadataIncludeOptions> {
                    this.customFields = includeCustom
                    this.totalCount = includeCount
                }
            }
        )
    }

    override fun getChannelMetadata(channel: String, includeCustom: Boolean): GetChannelMetadata {
        return GetChannelMetadataImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.include = createJsObject<PubNubJs.IncludeCustomFields> {
                    this.customFields = includeCustom
                }
            }
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
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.data = createChannelMetadata(
                    PatchValue.of(name),
                    PatchValue.of(description),
                    PatchValue.of(status),
                    PatchValue.of(type),
                    PatchValue.of(custom),
                )
                this.ifMatchesEtag = ifMatchesEtag

                this.include = createJsObject<PubNubJs.UuidIncludeCustom> {
                    this.customFields = includeCustom
                }
            }
        )
    }

    override fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        return RemoveChannelMetadataImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
            }
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
            jsPubNub,
            createJsObject {
                this.limit = limit
                this.page = page.toMetadataPage()
                this.filter = filter
                this.include = createJsObject<PubNubJs.MetadataIncludeOptions> {
                    this.customFields = includeCustom
                    this.totalCount = includeCount
                }
                this.sort = sort.toJsMap()
            }
        )
    }

    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): GetUUIDMetadata {
        return GetUUIDMetadataImpl(
            jsPubNub,
            createJsObject {
                this.uuid = uuid
                this.include = createJsObject<PubNubJs.UuidIncludeCustom> {
                    this.customFields = customFields
                }
            }
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
            jsPubNub,
            createJsObject {
                data = createUuidMetadata(
                    PatchValue.of(name),
                    PatchValue.of(externalId),
                    PatchValue.of(profileUrl),
                    PatchValue.of(email),
                    PatchValue.of(status),
                    PatchValue.of(type),
                    PatchValue.of(custom),
                )
                this.uuid = uuid
                this.ifMatchesEtag = ifMatchesEtag

                include = createJsObject<PubNubJs.UuidIncludeCustom> {
                    this.customFields = includeCustom
                }
            }
        )
    }

    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
        return RemoveUUIDMetadataImpl(
            jsPubNub,
            createJsObject {
                this.uuid = uuid
            }
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
        return GetMembershipsImpl(
            jsPubNub,
            createJsObject {
                uuid?.let { this.uuid = it }
                sort.takeIf { it.isNotEmpty() }?.toJsMap()?.let { this.sort = it }
                this.filter = filter
                page.toMetadataPage()?.let { this.page = it }
                this.include = createJsObject<PubNubJs.MembershipIncludeOptions> {
                    this.customFields = includeCustom
                    this.totalCount = includeCount
                    if (includeChannelDetails != null) {
                        this.channelFields = true
                        this.customChannelFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
                    }
                    this.channelTypeField = includeType
                    this.channelStatusField = true
                    this.statusField = true
                    // todo we don't have parameters for all fields here?
                }
                this.limit = limit
            }
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
            jsPubNub,
            createJsObject {
                userId?.let { this.uuid = it }
                this.sort = sort.toJsMap()
                this.filter = filter
                this.page = page.toMetadataPage()
                this.include = include.toMembershipIncludeOptions()
                this.limit = limit
            }
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
        return SetMembershipsImpl(
            jsPubNub,
            createJsObject {
                this.sort = sort.toJsMap()
                this.page = page.toMetadataPage()
                this.filter = filter
                this.include = createJsObject<PubNubJs.MembershipIncludeOptions> {
                    this.customFields = includeCustom
                    this.totalCount = includeCount
                    if (includeChannelDetails != null) {
                        this.channelFields = true
                        this.customChannelFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
                    }
                    this.channelTypeField = includeType
                    this.channelStatusField = true
                    this.statusField = true
                    // todo we don't have parameters for all fields here?
                }
                this.uuid = uuid
                this.channels = channels.map {
                    createJsObject<PubNubJs.SetCustom> {
                        this.id = it.channel
                        this.custom = it.custom?.adjustCollectionTypes()?.unsafeCast<PubNubJs.CustomObject>()
                        this.status = status // todo this doesn't seem to get to the server with JS, or cannot read it back
                    }
                }.toTypedArray()
                this.limit = limit
            }
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
        return SetMembershipsImpl(
            jsPubNub,
            createJsObject {
                this.sort = sort.toJsMap()
                this.page = page.toMetadataPage()
                this.filter = filter
                this.include = include.toMembershipIncludeOptions()
                this.uuid = userId
                this.channels = channels.map {
                    createJsObject<PubNubJs.SetCustom> {
                        this.id = it.channel
                        this.custom = it.custom?.adjustCollectionTypes()?.unsafeCast<PubNubJs.CustomObject>()
                        this.status = it.status // todo this doesn't seem to get to the server with JS, or cannot read it back
                        this.type = it.type
                    }
                }.toTypedArray()
                this.limit = limit
            }
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
        return RemoveMembershipsImpl(
            jsPubNub,
            createJsObject {
                this.sort = sort.toJsMap()
                this.page = page.toMetadataPage()
                this.filter = filter
                this.include = createJsObject<PubNubJs.MembershipIncludeOptions> {
                    this.customFields = includeCustom
                    this.totalCount = includeCount
                    if (includeChannelDetails != null) {
                        this.channelFields = true
                        this.customChannelFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
                    }
                    this.channelTypeField = includeType
                    this.channelStatusField = true
                    this.statusField = true
                    // todo we don't have parameters for all fields here?
                }
                this.uuid = uuid
                this.channels = channels.toTypedArray()
                this.limit = limit
            }
        )
    }

    override fun removeMemberships(
        channels: List<String>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude
    ): ManageMemberships {
        return RemoveMembershipsImpl(
            jsPubNub,
            createJsObject {
                this.sort = sort.toJsMap()
                this.page = page.toMetadataPage()
                this.filter = filter
                this.include = include.toMembershipIncludeOptions()
                this.uuid = userId
                this.channels = channels.toTypedArray()
                this.limit = limit
            }
        )
    }

// TODO doesn't exist in JS
//    override fun manageMemberships(
//        channelsToSet: List<ChannelMembershipInput>,
//        channelsToRemove: List<String>,
//        uuid: String?,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMembershipKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeChannelDetails: PNChannelDetailsLevel?
//    ): ManageMemberships {
//        TODO("Not yet implemented")
//    }

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
        return GetChannelMembersImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.limit = limit
                this.page = page.toMetadataPage()
                this.filter = filter
                this.include = createJsObject<PubNubJs.IncludeOptions> {
                    if (includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM) {
                        this.UUIDFields = true
                    }
                    if (includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM) {
                        this.customUUIDFields = true
                    }
                    this.customFields = includeCustom
                    this.totalCount = includeCount
                    this.UUIDTypeField = includeType
                    this.UUIDStatusField = true
                    this.statusField = true
                    // todo we don't have parameters for all fields here
                }
                this.sort = sort.toJsMap()
            }
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
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.limit = limit
                this.page = page.toMetadataPage()
                this.filter = filter
                this.include = include.toMemberIncludeOptions()
                this.sort = sort.toJsMap()
            }
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
        return SetChannelMembersImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.uuids = uuids.map {
                    createJsObject<PubNubJs.SetCustom> {
                        this.id = it.uuid
                        this.custom = it.custom?.adjustCollectionTypes()?.unsafeCast<PubNubJs.CustomObject>()
                        this.status = it.status
                    }
                }.toTypedArray()
                this.limit = limit
                this.page = page.toMetadataPage()
                this.filter = filter
                this.sort = sort.toJsMap()
                this.include = createJsObject<PubNubJs.IncludeOptions> {
                    this.totalCount = includeCount
                    this.customFields = includeCustom
                    this.statusField = true
                    if (includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM) {
                        this.UUIDFields = true
                    }
                    if (includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM) {
                        this.customUUIDFields = true
                    }
                    this.UUIDTypeField = includeType
                    this.UUIDStatusField = true
                    this.statusField = true
                    // todo we don't have parameters for all fields here
                }
            }
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
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.uuids = users.map {
                    createJsObject<PubNubJs.SetCustom> {
                        this.id = it.uuid
                        this.custom = it.custom?.adjustCollectionTypes()?.unsafeCast<PubNubJs.CustomObject>()
                        this.status = it.status
                        this.type = it.type
                    }
                }.toTypedArray()
                this.limit = limit
                this.page = page.toMetadataPage()
                this.filter = filter
                this.sort = sort.toJsMap()
                this.include = include.toMemberIncludeOptions()
            }
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
        return RemoveChannelMembersImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.uuids = uuids.toTypedArray()
                this.limit = limit
                this.page = page.toMetadataPage()
                this.filter = filter
                this.sort = sort.toJsMap()
                this.include = createJsObject<PubNubJs.IncludeOptions> {
                    this.totalCount = includeCount
                    this.customFields = includeCustom
                    this.statusField = true
                    if (includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM) {
                        this.UUIDFields = true
                    }
                    if (includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM) {
                        this.customUUIDFields = true
                    }
                    this.UUIDTypeField = includeType
                    this.UUIDStatusField = true
                    this.statusField = true
                    // todo we don't have parameters for all fields here
                }
            }
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
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.uuids = userIds.toTypedArray()
                this.limit = limit
                this.page = page.toMetadataPage()
                this.filter = filter
                this.sort = sort.toJsMap()
                this.include = include.toMemberIncludeOptions()
            }
        )
    }

//    override fun manageChannelMembers(
//        channel: String,
//        uuidsToSet: Collection<MemberInput>,
//        uuidsToRemove: Collection<String>,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMemberKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeUUIDDetails: PNUUIDDetailsLevel?
//    ): ManageChannelMembers {
//        TODO("Not yet implemented")
//    }

    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): ListFiles {
        return ListFilesImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.limit = limit
                if (next != null) {
                    this.next = next.pageHash
                }
            }
        )
    }

    override fun getFileUrl(channel: String, fileName: String, fileId: String): GetFileUrl {
        return GetFileUrlImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.name = fileName
                this.id = fileId
            }
        )
    }

    override fun deleteFile(channel: String, fileName: String, fileId: String): DeleteFile {
        return DeleteFileImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.name = fileName
                this.id = fileId
            }
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
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.fileName = fileName
                this.fileId = fileId
                this.message = message?.adjustCollectionTypes()
                this.meta = meta?.adjustCollectionTypes()
                this.ttl = ttl
                this.storeInHistory = shouldStore
                this.customMessageType = customMessageType
            }
        )
    }

    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        jsPubNub.subscribe(
            createJsObject {
                this.channels = channels.toTypedArray()
                this.channelGroups = channelGroups.toTypedArray()
                this.withPresence = withPresence
                this.timetoken = withTimetoken.adjustCollectionTypes() as? String
            }
        )
    }

    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
        jsPubNub.unsubscribe(
            createJsObject {
                this.channels = channels.toTypedArray()
                this.channelGroups = channelGroups.toTypedArray()
            }
        )
    }

    override fun unsubscribeAll() {
        jsPubNub.unsubscribeAll()
    }

    override fun setToken(token: String?) {
        jsPubNub.setToken(token)
    }

    override fun getToken(): String? {
        return jsPubNub.getToken()
    }

    override fun destroy() {
        jsPubNub.destroy()
    }

    override fun channel(name: String): Channel {
        return ChannelImpl(jsPubNub.asDynamic().channel(name))
    }

    override fun channelGroup(name: String): ChannelGroup {
        return ChannelGroupImpl(jsPubNub.asDynamic().channelGroup(name))
    }

    override fun channelMetadata(id: String): ChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun userMetadata(id: String): UserMetadata {
        TODO("Not yet implemented")
    }

    override fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet {
        TODO("Not yet implemented")
    }

    override fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions
    ): SubscriptionSet {
        val params = createJsObject<PubNubJs.SubscriptionSetParams> {
            this.channels = channels.toTypedArray()
            this.channelGroups = channelGroups.toTypedArray()
            this.subscriptionOptions = createJsObject {
                if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                    receivePresenceEvents = true
                }
            }
        }
        return SubscriptionSetImpl(jsPubNub.asDynamic().subscriptionSet(params))
    }

    override fun parseToken(token: String): PNToken {
        val jsToken = jsPubNub.parseToken(token)
        return PNToken(
            jsToken.version.toInt(),
            jsToken.timestamp.toLong(),
            jsToken.ttl.toLong(),
            jsToken.authorized_uuid,
            jsToken.resources?.let {
                PNToken.PNTokenResources(
                    it.channels.toKmp(),
                    it.groups.toKmp(),
                    it.uuids.toKmp(),
                )
            } ?: PNToken.PNTokenResources(),
            jsToken.patterns?.let {
                PNToken.PNTokenResources()
            } ?: PNToken.PNTokenResources()
        )
    }

    private fun JsMap<PubNubJs.GrantTokenPermissions>?.toKmp() = this?.toMap()?.mapValues { entry ->
        PNToken.PNResourcePermissions(
            entry.value.read ?: false,
            entry.value.write ?: false,
            entry.value.manage ?: false,
            entry.value.delete ?: false,
            entry.value.get ?: false,
            entry.value.update ?: false,
            entry.value.join ?: false
        )
    } ?: emptyMap()

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
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.file = inputStream.fileInput
                this.message = message
                this.meta = meta
                this.ttl = ttl
                this.storeInHistory = shouldStore
                this.cipherKey = cipherKey
                this.customMessageType = customMessageType
            }
        )
    }

    override fun downloadFile(channel: String, fileName: String, fileId: String, cipherKey: String?): DownloadFile {
        return DownloadFileImpl(
            jsPubNub,
            createJsObject {
                this.channel = channel
                this.name = fileName
                this.id = fileId
                this.cipherKey = cipherKey
            }
        )
    }
}

fun Any.adjustCollectionTypes(): Any {
    return when (this) {
        is Map<*, *> -> {
            val json = json()
            entries.forEach {
                json[it.key.toString()] = it.value?.adjustCollectionTypes()
            }
            json
        }
        is Collection<*> -> {
            this.map { it?.adjustCollectionTypes() }.toTypedArray()
        }
        is Array<*> -> {
            this.map { it?.adjustCollectionTypes() }.toTypedArray()
        }
        is Long -> {
            return toString()
        }
        else -> this
    }
}

private fun createUuidMetadata(
    name: PatchValue<String?>,
    externalId: PatchValue<String?>,
    profileUrl: PatchValue<String?>,
    email: PatchValue<String?>,
    status: PatchValue<String?>,
    type: PatchValue<String?>,
    custom: PatchValue<Map<String, Any?>?>
): PubNubJs.UUIDMetadata {
    val result: PubNubJs.UUIDMetadata = createJsObject()
    name.value?.let { result.name = it }
    externalId.value?.let { result.externalId = it }
    profileUrl.value?.let { result.profileUrl = it }
    email.value?.let { result.email = it }
    status.value?.let { result.status = it }
    type.value?.let { result.type = it }
    custom.value?.let { result.custom = it.adjustCollectionTypes().unsafeCast<PubNubJs.CustomObject>() }
    return result
}

private fun createChannelMetadata(
    name: PatchValue<String?>,
    description: PatchValue<String?>,
    status: PatchValue<String?>,
    type: PatchValue<String?>,
    custom: PatchValue<Map<String, Any?>?>
): PubNubJs.ChannelMetadata {
    val result: PubNubJs.ChannelMetadata = createJsObject()
    name.value?.let { result.name = it }
    description.value?.let { result.description = it }
    status.value?.let { result.status = it }
    type.value?.let { result.type = it }
    custom.value?.let { result.custom = it.adjustCollectionTypes().unsafeCast<PubNubJs.CustomObject>() }
    return result
}
//
// fun Map<String, Any?>.toJsObject(): PubNubJs.CustomObject {
//    val custom = createJsObject<dynamic> {  }
//    entries.forEach {
//        custom[it.key] = it.value
//    }
//    @Suppress("UnsafeCastFromDynamic")
//    return custom
// }

fun PNConfiguration.toJs(): PubNubJs.PNConfiguration {
    val config: PubNubJs.PNConfiguration = createJsObject()
    config.userId = userId.value
    config.subscribeKey = subscribeKey
    config.publishKey = publishKey
    config.secretKey = secretKey
    config.logVerbosity = logVerbosity == PNLogVerbosity.BODY
    config.enableEventEngine = enableEventEngine
//    config.authKey
//    config.ssl: Boolean?
//    config.origin: dynamic /* String? | Array<String>? */
//    config.presenceTimeout: Number?
//    config.heartbeatInterval: Number?
//    config.restore: Boolean?
//    config.keepAlive: Boolean?
//    config.keepAliveSettings: KeepAliveSettings?
//    config.subscribeRequestTimeout: Number?
//    config.suppressLeaveEvents: Boolean?
//    config.secretKey: String?
//    config.requestMessageCountThreshold: Number?
//    config.autoNetworkDetection: Boolean?
//    config.listenToBrowserNetworkEvents: Boolean?
//    config.useRandomIVs: Boolean?
//    config.dedupeOnSubscribe: Boolean?
//    config.cryptoModule: CryptoModule?
//    config.retryConfiguration: dynamic /* LinearRetryPolicyConfiguration? | ExponentialRetryPolicyConfiguration? */
//    config.enableEventEngine: Boolean?
//    config.maintainPresenceState: Boolean?
    return config
}

private inline fun <reified T : PNAbstractGrant> getGrantTokenPermissions(grants: List<PNGrant>) =
    grants.filterIsInstance<T>().associate {
        it.id to createJsObject<PubNubJs.GrantTokenPermissions> {
            this.get = it.get
            this.join = it.join
            this.delete = it.delete
            this.update = it.update
            this.write = it.write
            this.manage = it.manage
            this.read = it.read
            // todo what about create? any other?
        }
    }.toJsMap()

private fun Collection<PNSortKey<out SortField>>.toJsMap() = associateBy(
    keySelector = { pnSortKey -> pnSortKey.key.fieldName },
    valueTransform = { pnSortKey -> pnSortKey.dir }
).toJsMap()

private fun PNPage?.toMetadataPage(): PubNubJs.MetadataPage? =
    this?.let { pageNotNull ->
        createJsObject<PubNubJs.MetadataPage> {
            if (pageNotNull is PNPage.PNNext) {
                this.next = pageNotNull.pageHash
            } else {
                this.prev = pageNotNull.pageHash
            }
        }
    }

private fun MembershipInclude.toMembershipIncludeOptions(): PubNubJs.MembershipIncludeOptions {
    return createJsObject<PubNubJs.MembershipIncludeOptions> {
        this.customFields = this@toMembershipIncludeOptions.includeCustom
        this.totalCount = this@toMembershipIncludeOptions.includeTotalCount
        this.channelFields = this@toMembershipIncludeOptions.includeChannel
        this.customChannelFields = this@toMembershipIncludeOptions.includeChannelCustom
        this.channelTypeField = this@toMembershipIncludeOptions.includeChannelType
        this.channelStatusField = this@toMembershipIncludeOptions.includeChannelStatus
        this.statusField = this@toMembershipIncludeOptions.includeStatus
        this.typeField = this@toMembershipIncludeOptions.includeType
    }
}

private fun MemberInclude.toMemberIncludeOptions(): PubNubJs.IncludeOptions {
    return createJsObject<PubNubJs.IncludeOptions> {
        this.totalCount = this@toMemberIncludeOptions.includeTotalCount
        this.customFields = this@toMemberIncludeOptions.includeCustom
        this.UUIDFields = this@toMemberIncludeOptions.includeUser
        this.customUUIDFields = this@toMemberIncludeOptions.includeUserCustom
        this.UUIDTypeField = this@toMemberIncludeOptions.includeUserType
        this.UUIDStatusField = this@toMemberIncludeOptions.includeUserStatus
        this.statusField = this@toMemberIncludeOptions.includeStatus
        this.typeField = this@toMemberIncludeOptions.includeType
    }
}
