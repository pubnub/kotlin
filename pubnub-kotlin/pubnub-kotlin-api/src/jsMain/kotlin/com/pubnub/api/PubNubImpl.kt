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
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import com.pubnub.api.models.consumer.access_manager.v3.PNAbstractGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNDataSyncProjectionScope
import com.pubnub.api.models.consumer.access_manager.v3.PNDataSyncProjections
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.TokenGrant
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
import com.pubnub.api.v2.entities.DataSyncChannel
import com.pubnub.api.v2.entities.DataSyncEntity
import com.pubnub.api.v2.entities.DataSyncUser
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
import com.pubnub.kmp.entriesOf
import com.pubnub.kmp.toJsMap
import com.pubnub.kmp.toMap
import kotlin.js.Json
import kotlin.js.json
import PubNub as PubNubJs

class PubNubImpl(val jsPubNub: PubNubJs) : PubNub {
    constructor(configuration: PNConfiguration) : this(PubNubJs(configuration.toJs()))

    override val dataSync: com.pubnub.api.datasync.DataSync = com.pubnub.api.datasync.DataSyncImpl()

    override val configuration: PNConfiguration
        get() = createPNConfiguration( // todo test this!
            userId = UserId(jsPubNub.getUUID()),
            subscribeKey = jsPubNub.asDynamic().configuration.subscribeKey,
            publishKey = jsPubNub.asDynamic().configuration.publishKey,
            secretKey = jsPubNub.asDynamic().configuration.secretKey,
            logLevel = fromJsEnum(jsPubNub.asDynamic().configuration.logLevel),
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

    override fun reconnect(timetoken: Long) {
        jsPubNub.reconnect(createJsObject { this.timetoken = timetoken.toString() })
    }

    override fun disconnect() {
        jsPubNub.disconnect()
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
        includeUUIDs: Boolean,
        limit: Int,
        offset: Int?
    ): HereNow {
        return HereNowImpl(
            jsPubNub,
            createJsObject {
                // todo handle limit and offset
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
        requireNoPnProjectionsInMeta(meta)
        return GrantTokenImpl(
            jsPubNub,
            buildLegacyGrantTokenParams(ttl, meta, authorizedUUID, channels, channelGroups, uuids)
        )
    }

    override fun grantToken(
        ttl: Int,
        authorizedUserId: UserId?,
        meta: CustomObject?,
        grants: List<TokenGrant>
    ): GrantToken {
        requireNoPnProjectionsInMeta(meta)
        return GrantTokenImpl(
            jsPubNub,
            buildFlatGrantTokenParams(ttl, authorizedUserId, meta, grants)
        )
    }

    // Same rule as the JVM GrantTokenRequestBody: `pn-projections` is owned by the SDK and set only through the
    // grants' `projection`, never through caller meta.
    private fun requireNoPnProjectionsInMeta(meta: CustomObject?) {
        if (meta != null && meta.containsKey(DataSyncNamespace.PN_PROJECTIONS)) {
            throw PubNubException(
                "`meta` must not contain `${DataSyncNamespace.PN_PROJECTIONS}`: set projections through the " +
                    "`projection` of DataSyncGrant.entity/relationship/channel/user (and their pattern variants)."
            )
        }
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
                        it.custom?.let { custom -> this.custom = custom.adjustCollectionTypes().unsafeCast<PubNubJs.CustomObject>() }
                        it.status?.let { status -> this.status = status }
                        it.type?.let { type -> this.type = type }
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
                        it.custom?.let { custom -> this.custom = custom.adjustCollectionTypes().unsafeCast<PubNubJs.CustomObject>() }
                        it.status?.let { status -> this.status = status }
                        it.type?.let { type -> this.type = type }
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

    override fun dataSyncUser(id: String): DataSyncUser =
        throw NotImplementedError("DataSync realtime subscribe is not implemented on the JS target")

    override fun dataSyncChannel(id: String): DataSyncChannel =
        throw NotImplementedError("DataSync realtime subscribe is not implemented on the JS target")

    override fun dataSyncEntity(id: String): DataSyncEntity =
        throw NotImplementedError("DataSync realtime subscribe is not implemented on the JS target")

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
        return jsPubNub.parseToken(token).toPNToken()
    }

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
    config.logLevel = logLevel.toJsEnum()
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

internal fun buildLegacyGrantTokenParams(
    ttl: Int,
    meta: CustomObject?,
    authorizedUUID: String?,
    channels: List<ChannelGrant>,
    channelGroups: List<ChannelGroupGrant>,
    uuids: List<UUIDGrant>,
): PubNubJs.GrantTokenParameters =
    createJsObject {
        this.meta = meta?.toJsMeta()
        this.ttl = ttl
        this.authorized_uuid = authorizedUUID
        this.resources = createJsObject<PubNubJs.PatternsOrResources> {
            this.channels = getGrantTokenPermissions<PNResourceGrant>(channels).toJsMap()
            this.groups = getGrantTokenPermissions<PNResourceGrant>(channelGroups).toJsMap()
            this.uuids = getGrantTokenPermissions<PNResourceGrant>(uuids).toJsMap()
        }
        this.patterns = createJsObject<PubNubJs.PatternsOrResources> {
            this.channels = getGrantTokenPermissions<PNPatternGrant>(channels).toJsMap()
            this.groups = getGrantTokenPermissions<PNPatternGrant>(channelGroups).toJsMap()
            this.uuids = getGrantTokenPermissions<PNPatternGrant>(uuids).toJsMap()
        }
    }

/**
 * Builds the npm `grantToken` parameters of the flat `grants` overload, routed like the JVM `GrantTokenRequestBody`:
 * - [ChannelGrant]s and `DataSyncGrant.channel` grants share the `channels` bucket, `DataSyncGrant.user` grants go to
 *   `users`, and entity/relationship/membership grants go to `dataSync.*`;
 * - projections go to `dataSyncProjections`, from which npm builds `meta.pn-projections` (same
 *   `datasync:<type>:<id>` keys as the JVM). The caller meta can't hold `pn-projections` (rejected before this), so
 *   npm replacing that key wholesale drops nothing.
 *
 * Empty buckets are left out rather than set to `null`: npm checks buckets by key presence (e.g. it rejects `users`
 * together with `uuids`). `uuids` is never sent, since the flat overload has no UUID grants.
 */
internal fun buildFlatGrantTokenParams(
    ttl: Int,
    authorizedUserId: UserId?,
    meta: CustomObject?,
    grants: List<TokenGrant>,
): PubNubJs.GrantTokenParameters {
    val buckets = FlatGrantBuckets()
    grants.forEach { grant ->
        when (grant) {
            is ChannelGrant -> buckets.channels.add(grant)
            is ChannelGroupGrant -> buckets.channelGroups.add(grant)
            is DataSyncGrantType -> when (grant.namespace) {
                DataSyncNamespace.CHANNELS_PROJECTION -> buckets.channels.add(grant)
                DataSyncNamespace.USERS_PROJECTION -> buckets.users.add(grant)
                DataSyncNamespace.ENTITIES -> buckets.entities.add(grant)
                DataSyncNamespace.RELATIONSHIPS -> buckets.relationships.add(grant)
                DataSyncNamespace.MEMBERSHIPS -> buckets.memberships.add(grant)
                else -> throw UnsupportedOperationException("Unknown DataSync namespace: ${grant.namespace}.")
            }
            // Throw rather than silently drop a grant, which would mint a weaker token than requested.
            else -> throw UnsupportedOperationException(
                "The JS target's grantToken doesn't support ${grant::class.simpleName}."
            )
        }
    }
    val dataSyncGrants = grants.filterIsInstance<DataSyncGrantType>()
    val resourceProjections = toJsProjectionScope<PNResourceGrant>(dataSyncGrants)
    val patternProjections = toJsProjectionScope<PNPatternGrant>(dataSyncGrants)
    return createJsObject {
        this.meta = meta?.toJsMeta()
        this.ttl = ttl
        this.authorized_uuid = authorizedUserId?.value
        this.resources = toJsGrantScopes<PNResourceGrant>(buckets)
        this.patterns = toJsGrantScopes<PNPatternGrant>(buckets)
        if (resourceProjections != null || patternProjections != null) {
            this.dataSyncProjections = createJsObject<PubNubJs.DataSyncProjections> {
                resourceProjections?.let { this.resources = it }
                patternProjections?.let { this.patterns = it }
            }
        }
    }
}

private class FlatGrantBuckets {
    val channels = ArrayList<PNGrant>()
    val channelGroups = ArrayList<PNGrant>()
    val users = ArrayList<PNGrant>()
    val entities = ArrayList<PNGrant>()
    val relationships = ArrayList<PNGrant>()
    val memberships = ArrayList<PNGrant>()
}

private inline fun <reified T : PNAbstractGrant> toJsGrantScopes(
    buckets: FlatGrantBuckets
): PubNubJs.PatternsOrResources =
    createJsObject {
        getGrantTokenPermissions<T>(buckets.channels).toJsMapOrNull()?.let { this.channels = it }
        getGrantTokenPermissions<T>(buckets.channelGroups).toJsMapOrNull()?.let { this.groups = it }
        getGrantTokenPermissions<T>(buckets.users).toJsMapOrNull()?.let { this.users = it }
        val entityPermissions = getGrantTokenPermissions<T>(buckets.entities).toJsMapOrNull()
        val relationshipPermissions = getGrantTokenPermissions<T>(buckets.relationships).toJsMapOrNull()
        val membershipPermissions = getGrantTokenPermissions<T>(buckets.memberships).toJsMapOrNull()
        if (entityPermissions != null || relationshipPermissions != null || membershipPermissions != null) {
            this.dataSync = createJsObject<PubNubJs.DataSyncTokenScopes> {
                entityPermissions?.let { this.entities = it }
                relationshipPermissions?.let { this.relationships = it }
                membershipPermissions?.let { this.memberships = it }
            }
        }
    }

// Projections are passed verbatim (only `null` is skipped) and the last one for an id wins, as in the JVM
// `GrantTokenRequestBody.mergeProjectionsIntoMeta`.
private inline fun <reified T : PNAbstractGrant> toJsProjectionScope(
    grants: List<DataSyncGrantType>
): PubNubJs.DataSyncProjectionScope? {
    val withProjection = grants.filter { it is T && it.projection != null }
    if (withProjection.isEmpty()) {
        return null
    }
    return createJsObject<PubNubJs.DataSyncProjectionScope> {
        projectionsIn(withProjection, DataSyncNamespace.ENTITIES)?.let { this.entities = it }
        projectionsIn(withProjection, DataSyncNamespace.RELATIONSHIPS)?.let { this.relationships = it }
        projectionsIn(withProjection, DataSyncNamespace.USERS_PROJECTION)?.let { this.users = it }
        projectionsIn(withProjection, DataSyncNamespace.CHANNELS_PROJECTION)?.let { this.channels = it }
        // No memberships: membership grants never carry a projection.
    }
}

private fun projectionsIn(grants: List<DataSyncGrantType>, namespace: String): JsMap<String>? =
    grants.filter { it.namespace == namespace }
        .mapNotNull { grant -> grant.projection?.let { grant.id to it } }
        .toMap()
        .toJsMapOrNull()

// Grants for the same id are OR-merged per flag, like the JVM `GrantTokenRequestBody`, so a later grant never clears
// a bit that an earlier one set.
private inline fun <reified T : PNAbstractGrant> getGrantTokenPermissions(
    grants: List<PNGrant>
): Map<String, PubNubJs.GrantTokenPermissions> =
    grants.filterIsInstance<T>().groupBy { it.id }.mapValues { (_, sameId) ->
        createJsObject<PubNubJs.GrantTokenPermissions> {
            this.get = sameId.any { it.get }
            this.join = sameId.any { it.join }
            this.delete = sameId.any { it.delete }
            this.update = sameId.any { it.update }
            this.write = sameId.any { it.write }
            this.manage = sameId.any { it.manage }
            this.read = sameId.any { it.read }
            this.create = sameId.any { it.create }
        }
    }

private fun <V> Map<String, V>.toJsMapOrNull(): JsMap<V>? = takeIf { it.isNotEmpty() }?.toJsMap()

private fun CustomObject.toJsMeta(): Json = toJsMetaValue().unsafeCast<Json>()

// Deep conversion, so nested caller meta reaches npm as plain JSON: maps → JS objects, collections/arrays → JS arrays.
private fun Any?.toJsMetaValue(): Any? =
    when (this) {
        is Map<*, *> -> {
            val source = this
            createJsObject<dynamic> {
                source.forEach { (key, value) -> this[key.toString()] = value.toJsMetaValue() }
            }
        }
        is Collection<*> -> map { it.toJsMetaValue() }.toTypedArray()
        is Array<*> -> map { it.toJsMetaValue() }.toTypedArray()
        else -> this
    }

/**
 * Maps a token parsed by npm to [PNToken], including patterns, `users`, `dataSync.*`, [PNToken.meta] and
 * [PNToken.projections]. Two npm differences from the JVM `TokenParser`:
 * - npm doesn't decode the CREATE bit for `channels` / `uuids`, so their `create` is always `false` here; `users`
 *   and `dataSync.*` do decode it.
 * - numbers in [PNToken.meta] are JS numbers (`Double`), where the JVM gives `Long` / `BigInteger`.
 */
internal fun PubNubJs.ParsedGrantToken.toPNToken(): PNToken {
    val kotlinMeta = meta?.fromJsMetaValue()
    return PNToken(
        version = version.toInt(),
        timestamp = timestamp.toLong(),
        ttl = ttl.toLong(),
        authorizedUUID = authorized_uuid,
        resources = resources.toPNTokenResources(),
        patterns = patterns.toPNTokenResources(),
        meta = kotlinMeta,
        projections = parseProjections(kotlinMeta),
    )
}

private fun PubNubJs.PatternsOrResources?.toPNTokenResources(): PNToken.PNTokenResources {
    if (this == null) {
        return PNToken.PNTokenResources()
    }
    return PNToken.PNTokenResources(
        channels = channels.toKmp(),
        channelGroups = groups.toKmp(),
        uuids = uuids.toKmp(),
        users = users.toKmp(),
        datasyncEntities = dataSync?.entities.toKmp(),
        datasyncRelationships = dataSync?.relationships.toKmp(),
        datasyncMemberships = dataSync?.memberships.toKmp(),
    )
}

private fun JsMap<PubNubJs.GrantTokenPermissions>?.toKmp() = this?.toMap()?.mapValues { entry ->
    PNToken.PNResourcePermissions(
        read = entry.value.read ?: false,
        write = entry.value.write ?: false,
        manage = entry.value.manage ?: false,
        delete = entry.value.delete ?: false,
        get = entry.value.get ?: false,
        update = entry.value.update ?: false,
        join = entry.value.join ?: false,
        create = entry.value.create ?: false,
    )
} ?: emptyMap()

// Inverse of `toJsMetaValue`: JS objects → maps, arrays → lists, the shape the JVM `TokenParser` gives.
private fun Any?.fromJsMetaValue(): Any? =
    when (this) {
        null -> null
        is Array<*> -> map { it.fromJsMetaValue() }
        else -> if (jsTypeOf(this) == "object") {
            entriesOf(unsafeCast<JsMap<Any?>>()).associate { (key, value) -> key to value.fromJsMetaValue() }
        } else {
            this
        }
    }

// Same decode as the JVM `TokenParser.parseProjections`: match the known namespace prefix instead of splitting on `:`
// (relationship and membership ids contain colons). Keys of an unknown namespace are ignored, as on the JVM.
private fun parseProjections(meta: Any?): PNDataSyncProjections? {
    val projectionsBlock = (meta as? Map<*, *>)?.get(DataSyncNamespace.PN_PROJECTIONS) as? Map<*, *> ?: return null
    return PNDataSyncProjections(
        resources = (projectionsBlock["res"] as? Map<*, *>).toPNProjectionScope(),
        patterns = (projectionsBlock["pat"] as? Map<*, *>).toPNProjectionScope(),
    )
}

private fun Map<*, *>?.toPNProjectionScope(): PNDataSyncProjectionScope {
    if (this == null) {
        return PNDataSyncProjectionScope()
    }
    val entities = LinkedHashMap<String, String>()
    val relationships = LinkedHashMap<String, String>()
    val memberships = LinkedHashMap<String, String>()
    val users = LinkedHashMap<String, String>()
    val channels = LinkedHashMap<String, String>()
    for ((rawKey, rawValue) in this) {
        val key = rawKey.toString()
        val projection = rawValue.toString()
        when {
            key.startsWith("${DataSyncNamespace.ENTITIES}:") ->
                entities[key.removePrefix("${DataSyncNamespace.ENTITIES}:")] = projection
            key.startsWith("${DataSyncNamespace.RELATIONSHIPS}:") ->
                relationships[key.removePrefix("${DataSyncNamespace.RELATIONSHIPS}:")] = projection
            key.startsWith("${DataSyncNamespace.MEMBERSHIPS}:") ->
                memberships[key.removePrefix("${DataSyncNamespace.MEMBERSHIPS}:")] = projection
            key.startsWith("${DataSyncNamespace.USERS_PROJECTION}:") ->
                users[key.removePrefix("${DataSyncNamespace.USERS_PROJECTION}:")] = projection
            key.startsWith("${DataSyncNamespace.CHANNELS_PROJECTION}:") ->
                channels[key.removePrefix("${DataSyncNamespace.CHANNELS_PROJECTION}:")] = projection
        }
    }
    return PNDataSyncProjectionScope(
        entities = entities,
        relationships = relationships,
        memberships = memberships,
        users = users,
        channels = channels,
    )
}

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

/**
 * Converts Kotlin LogLevel enum to JavaScript SDK's PubNub.LogLevel enum value.
 * @internal
 */
internal fun com.pubnub.api.enums.LogLevel.toJsEnum(): dynamic = when (this) {
    com.pubnub.api.enums.LogLevel.NONE -> PubNubJs.LogLevel.None
    com.pubnub.api.enums.LogLevel.ERROR -> PubNubJs.LogLevel.Error
    com.pubnub.api.enums.LogLevel.WARN -> PubNubJs.LogLevel.Warn
    com.pubnub.api.enums.LogLevel.INFO -> PubNubJs.LogLevel.Info
    com.pubnub.api.enums.LogLevel.DEBUG -> PubNubJs.LogLevel.Debug
    com.pubnub.api.enums.LogLevel.TRACE -> PubNubJs.LogLevel.Trace
    else -> {}
}

/**
 * Converts JavaScript SDK's PubNub.LogLevel enum value to Kotlin LogLevel enum.
 * @internal
 */
internal fun fromJsEnum(jsLogLevel: dynamic): com.pubnub.api.enums.LogLevel {
    return when (jsLogLevel) {
        PubNubJs.LogLevel.None -> com.pubnub.api.enums.LogLevel.NONE
        PubNubJs.LogLevel.Error -> com.pubnub.api.enums.LogLevel.ERROR
        PubNubJs.LogLevel.Warn -> com.pubnub.api.enums.LogLevel.WARN
        PubNubJs.LogLevel.Info -> com.pubnub.api.enums.LogLevel.INFO
        PubNubJs.LogLevel.Debug -> com.pubnub.api.enums.LogLevel.DEBUG
        PubNubJs.LogLevel.Trace -> com.pubnub.api.enums.LogLevel.TRACE
        else -> com.pubnub.api.enums.LogLevel.NONE // Default to NONE for unknown values
    }
}
