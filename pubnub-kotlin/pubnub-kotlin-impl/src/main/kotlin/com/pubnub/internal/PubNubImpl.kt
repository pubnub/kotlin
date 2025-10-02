package com.pubnub.internal

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.crypto.CryptoModule
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
import com.pubnub.api.logging.ErrorDetails
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.sum.toChannelGrant
import com.pubnub.api.models.consumer.access_manager.sum.toUuidGrant
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
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.crypto.CryptoModuleImpl
import com.pubnub.internal.crypto.decryptString
import com.pubnub.internal.crypto.encryptString
import com.pubnub.internal.endpoints.DeleteMessagesEndpoint
import com.pubnub.internal.endpoints.FetchMessagesEndpoint
import com.pubnub.internal.endpoints.HistoryEndpoint
import com.pubnub.internal.endpoints.MessageCountsEndpoint
import com.pubnub.internal.endpoints.TimeEndpoint
import com.pubnub.internal.endpoints.access.GrantEndpoint
import com.pubnub.internal.endpoints.access.GrantTokenEndpoint
import com.pubnub.internal.endpoints.access.RevokeTokenEndpoint
import com.pubnub.internal.endpoints.channel_groups.AddChannelChannelGroupEndpoint
import com.pubnub.internal.endpoints.channel_groups.AllChannelsChannelGroupEndpoint
import com.pubnub.internal.endpoints.channel_groups.DeleteChannelGroupEndpoint
import com.pubnub.internal.endpoints.channel_groups.ListAllChannelGroupEndpoint
import com.pubnub.internal.endpoints.channel_groups.RemoveChannelChannelGroupEndpoint
import com.pubnub.internal.endpoints.files.DeleteFileEndpoint
import com.pubnub.internal.endpoints.files.DownloadFileEndpoint
import com.pubnub.internal.endpoints.files.GenerateUploadUrlEndpoint
import com.pubnub.internal.endpoints.files.GetFileUrlEndpoint
import com.pubnub.internal.endpoints.files.ListFilesEndpoint
import com.pubnub.internal.endpoints.files.PublishFileMessageEndpoint
import com.pubnub.internal.endpoints.files.SendFileEndpoint
import com.pubnub.internal.endpoints.files.UploadFileEndpoint
import com.pubnub.internal.endpoints.message_actions.AddMessageActionEndpoint
import com.pubnub.internal.endpoints.message_actions.GetMessageActionsEndpoint
import com.pubnub.internal.endpoints.message_actions.RemoveMessageActionEndpoint
import com.pubnub.internal.endpoints.objects.channel.GetAllChannelMetadataEndpoint
import com.pubnub.internal.endpoints.objects.channel.GetChannelMetadataEndpoint
import com.pubnub.internal.endpoints.objects.channel.RemoveChannelMetadataEndpoint
import com.pubnub.internal.endpoints.objects.channel.SetChannelMetadataEndpoint
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.endpoints.objects.member.GetChannelMembersEndpoint
import com.pubnub.internal.endpoints.objects.member.ManageChannelMembersEndpoint
import com.pubnub.internal.endpoints.objects.membership.GetMembershipsEndpoint
import com.pubnub.internal.endpoints.objects.membership.ManageMembershipsEndpoint
import com.pubnub.internal.endpoints.objects.uuid.GetAllUUIDMetadataEndpoint
import com.pubnub.internal.endpoints.objects.uuid.GetUUIDMetadataEndpoint
import com.pubnub.internal.endpoints.objects.uuid.RemoveUUIDMetadataEndpoint
import com.pubnub.internal.endpoints.objects.uuid.SetUUIDMetadataEndpoint
import com.pubnub.internal.endpoints.presence.GetStateEndpoint
import com.pubnub.internal.endpoints.presence.HereNowEndpoint
import com.pubnub.internal.endpoints.presence.SetStateEndpoint
import com.pubnub.internal.endpoints.presence.WhereNowEndpoint
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint
import com.pubnub.internal.endpoints.pubsub.SignalEndpoint
import com.pubnub.internal.endpoints.push.AddChannelsToPushEndpoint
import com.pubnub.internal.endpoints.push.ListPushProvisionsEndpoint
import com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDeviceEndpoint
import com.pubnub.internal.endpoints.push.RemoveChannelsFromPushEndpoint
import com.pubnub.internal.logging.ConfigurationLogger.logConfiguration
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.managers.BasePathManager
import com.pubnub.internal.managers.DuplicationManager
import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.managers.MapperManager
import com.pubnub.internal.managers.PublishSequenceManager
import com.pubnub.internal.managers.RetrofitManager
import com.pubnub.internal.managers.TokenManager
import com.pubnub.internal.managers.TokenParser
import com.pubnub.internal.presence.Presence
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.internal.presence.eventengine.effect.effectprovider.HeartbeatProviderImpl
import com.pubnub.internal.presence.eventengine.effect.effectprovider.LeaveProviderImpl
import com.pubnub.internal.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.internal.subscribe.Subscribe
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.internal.v2.PNConfigurationImpl
import com.pubnub.internal.v2.entities.ChannelGroupImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelImpl
import com.pubnub.internal.v2.entities.ChannelMetadataImpl
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.entities.UserMetadataImpl
import com.pubnub.internal.v2.subscription.EmitterHelper
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.subscription.SubscriptionInternal
import com.pubnub.internal.v2.subscription.SubscriptionSetImpl
import com.pubnub.internal.workers.SubscribeMessageProcessor
import com.pubnub.kmp.CustomObject
import java.io.InputStream
import java.util.Date
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration.Companion.seconds

private const val PNSDK_PUBNUB_KOTLIN = "PubNub-Kotlin"

open class PubNubImpl(
    override val configuration: PNConfiguration,
    val pnsdkName: String = PNSDK_PUBNUB_KOTLIN,
    eventEnginesConf: EventEnginesConf = EventEnginesConf()
) : PubNub {
    protected val logger: PNLogger by lazy { LoggerManager.instance.getLogger(logConfig, this::class.java) }
    internal val tokenManager: TokenManager = TokenManager()

    init {
        this.setToken(configuration.authToken)
    }

    constructor(configuration: PNConfiguration) : this(configuration, PNSDK_PUBNUB_KOTLIN)

    /**
     * Unique id of this PubNub instance.
     *
     * @see [PNConfiguration.includeInstanceIdentifier]
     */
    val instanceId = UUID.randomUUID().toString()
    val logConfig: LogConfig = LogConfig(
        pnInstanceId = instanceId,
        userId = configuration.userId.value,
        customLoggers = configuration.customLoggers,
    )

    val mapper = MapperManager(logConfig)

    private val numberOfThreadsInPool = Integer.min(Runtime.getRuntime().availableProcessors(), 8)
    internal val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(numberOfThreadsInPool)
    val listenerManager: ListenerManager = ListenerManager(this)
    private val basePathManager = BasePathManager(configuration)
    internal val retrofitManager = RetrofitManager(pubnub = this, configuration = configuration)
    internal val publishSequenceManager = PublishSequenceManager(MAX_SEQUENCE)
    private val tokenParser: TokenParser = TokenParser()
    private val presenceData = PresenceData()
    private val subscribe =
        Subscribe.create(
            this,
            listenerManager,
            eventEnginesConf,
            SubscribeMessageProcessor(this, DuplicationManager(configuration), logConfig),
            presenceData,
            configuration.maintainPresenceState,
        )

    private val presence =
        Presence.create(
            heartbeatProvider = HeartbeatProviderImpl(this),
            leaveProvider = LeaveProviderImpl(this),
            heartbeatInterval = configuration.heartbeatInterval.seconds,
            suppressLeaveEvents = configuration.suppressLeaveEvents,
            heartbeatNotificationOptions = configuration.heartbeatNotificationOptions,
            listenerManager = listenerManager,
            eventEngineConf = eventEnginesConf.presence,
            presenceData = presenceData,
            sendStateWithHeartbeat = configuration.maintainPresenceState,
            executorService = executorService,
            logConfig = logConfig,
        )

    internal val cryptoModuleWithLogConfig: CryptoModule? by lazy {
        when (configuration) {
            is PNConfigurationImpl -> (configuration as PNConfigurationImpl).getCryptoModuleWithLogConfig(logConfig)
            else -> {
                // Try to call getCryptoModuleWithLogConfig using reflection for Java implementation
                try {
                    val method = configuration.javaClass.getMethod("getCryptoModuleWithLogConfig", LogConfig::class.java)
                    method.invoke(configuration, logConfig) as? CryptoModule
                } catch (e: Exception) {
                    logger.error(
                        LogMessage(
                            message = LogMessageContent.Error(
                                message = ErrorDetails(
                                    type = e.javaClass.simpleName,
                                    message = "Failed calling getCryptoModuleWithLogConfig"
                                )
                            ),
                            details = "details",
                        )
                    )
                    null
                }
            }
        }
    }

    //region Internal
    internal fun baseUrl() = basePathManager.basePath()

    internal fun requestId() = UUID.randomUUID().toString()
    //endregion

    fun generatePnsdk(): String {
        val joinedSuffixes = configuration.pnsdkSuffixes.toSortedMap().values.joinToString(" ")
        return "$pnsdkName/$SDK_VERSION" +
            if (joinedSuffixes.isNotBlank()) {
                " $joinedSuffixes"
            } else {
                ""
            }
    }

    private val emitterHelper = EmitterHelper(listenerManager)
    override var onMessage: ((PNMessageResult) -> Unit)? by emitterHelper::onMessage
    override var onPresence: ((PNPresenceEventResult) -> Unit)? by emitterHelper::onPresence
    override var onSignal: ((PNSignalResult) -> Unit)? by emitterHelper::onSignal
    override var onMessageAction: ((PNMessageActionResult) -> Unit)? by emitterHelper::onMessageAction
    override var onObjects: ((PNObjectEventResult) -> Unit)? by emitterHelper::onObjects
    override var onFile: ((PNFileEventResult) -> Unit)? by emitterHelper::onFile

    override val version: String
        get() = SDK_VERSION

    override val timestamp: Int
        get() = timestamp()

    override val baseUrl: String
        get() = baseUrl()

    companion object {
        internal const val TIMESTAMP_DIVIDER = 1000
        internal const val SDK_VERSION = PUBNUB_VERSION
        internal const val MAX_SEQUENCE = 65535

        @JvmStatic
        fun timestamp() = (Date().time / TIMESTAMP_DIVIDER).toInt()

        /**
         * Generates random UUID to use. You should set a unique UUID to identify the user or the device that connects to PubNub.
         */
        @JvmStatic
        fun generateUUID() = "pn-${UUID.randomUUID()}"
    }

    init {
        logConfiguration(
            configuration = configuration,
            logger = logger,
            instanceId = instanceId,
            className = PNConfigurationImpl::class.java
        )
    }

    override fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions,
    ): SubscriptionSet {
        val subscriptionSet = subscriptionSetOf(subscriptions = emptySet())
        channels.forEach {
            subscriptionSet.add(channel(it).subscription(options))
        }
        channelGroups.forEach {
            subscriptionSet.add(channelGroup(it).subscription(options))
        }
        return subscriptionSet
    }

    override fun removeAllListeners() {
        listenerManager.removeAllListeners()
    }

    override fun addListener(listener: SubscribeCallback) {
        listenerManager.addListener(listener)
    }

    override fun addListener(listener: StatusListener) {
        listenerManager.addListener(listener)
    }

    override fun addListener(listener: EventListener) {
        listenerManager.addListener(listener)
    }

    override fun removeListener(listener: Listener) {
        listenerManager.removeListener(listener)
    }

    override fun channel(name: String): ChannelImpl {
        return ChannelImpl(this, ChannelName(name))
    }

    override fun channelGroup(name: String): ChannelGroupImpl {
        return ChannelGroupImpl(this, ChannelGroupName(name))
    }

    override fun channelMetadata(id: String): ChannelMetadata {
        return ChannelMetadataImpl(this, ChannelName(id))
    }

    override fun userMetadata(id: String): UserMetadata {
        return UserMetadataImpl(this, ChannelName(id))
    }

    override fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet {
        return SubscriptionSetImpl(this, subscriptions as Set<SubscriptionInternal>)
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
    ): Publish =
        PublishEndpoint(
            pubnub = this,
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = shouldStore,
            usePost = usePost,
            replicate = replicate,
            ttl = ttl,
            customMessageType = customMessageType
        )

    override fun fire(channel: String, message: Any, meta: Any?, usePost: Boolean): Publish = publish(
        channel = channel,
        message = message,
        meta = meta,
        shouldStore = false,
        usePost = usePost,
        replicate = false,
    )

    @Deprecated(
        "`fire()` never used the `ttl` parameter, please use the version without `ttl`.",
        replaceWith = ReplaceWith("fire(channel, message, meta, usePost)")
    )
    override fun fire(
        channel: String,
        message: Any,
        meta: Any?,
        usePost: Boolean,
        ttl: Int?,
    ): Publish {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: fire() with ttl parameter is deprecated. Use fire(channel, message, meta, usePost) instead."
                ),
                details = "The ttl parameter is not used and this method will be removed in a future version",
            )
        )
        return fire(channel, message, meta, usePost)
    }

    override fun signal(
        channel: String,
        message: Any,
        customMessageType: String?,
    ): Signal = SignalEndpoint(pubnub = this, channel = channel, message = message, customMessageType = customMessageType)

    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): AddChannelsToPush {
        return AddChannelsToPushEndpoint(
            pubnub = this,
            pushType = pushType,
            channels = channels,
            deviceId = deviceId,
            topic = topic,
            environment = environment,
        )
    }

    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): ListPushProvisions {
        return ListPushProvisionsEndpoint(
            pubnub = this,
            pushType = pushType,
            deviceId = deviceId,
            topic = topic,
            environment = environment,
        )
    }

    override fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): RemoveChannelsFromPush {
        return RemoveChannelsFromPushEndpoint(
            pubnub = this,
            pushType = pushType,
            channels = channels,
            deviceId = deviceId,
            topic = topic,
            environment = environment,
        )
    }

    override fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): RemoveAllPushChannelsForDevice {
        return RemoveAllPushChannelsForDeviceEndpoint(
            pubnub = this,
            pushType = pushType,
            deviceId = deviceId,
            topic = topic,
            environment = environment,
        )
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "Use fetchMessages(List<String>, PNBoundedPage, Boolean, Boolean, Boolean, Boolean, Boolean) instead",
    )
    override fun history(
        channel: String,
        start: Long?,
        end: Long?,
        count: Int,
        reverse: Boolean,
        includeTimetoken: Boolean,
        includeMeta: Boolean,
    ): History {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: history() is deprecated. Use fetchMessages(List<String>, PNBoundedPage, Boolean, Boolean, Boolean, Boolean, Boolean) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return HistoryEndpoint(
            pubnub = this,
            channel = channel,
            start = start,
            end = end,
            count = count,
            reverse = reverse,
            includeTimetoken = includeTimetoken,
            includeMeta = includeMeta,
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
        return FetchMessagesEndpoint(
            pubnub = this,
            channels = channels,
            page = page,
            includeUUID = includeUUID,
            includeMeta = includeMeta,
            includeMessageActions = includeMessageActions,
            includeMessageType = includeMessageType,
            includeCustomMessageType = includeCustomMessageType
        )
    }

    override fun deleteMessages(
        channels: List<String>,
        start: Long?,
        end: Long?,
    ): DeleteMessages {
        return DeleteMessagesEndpoint(pubnub = this, channels = channels, start = start, end = end)
    }

    override fun messageCounts(
        channels: List<String>,
        channelsTimetoken: List<Long>,
    ): MessageCounts {
        return MessageCountsEndpoint(pubnub = this, channels = channels, channelsTimetoken = channelsTimetoken)
    }

    override fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean,
    ): HereNow {
        return HereNowEndpoint(
            pubnub = this,
            channels = channels,
            channelGroups = channelGroups,
            includeState = includeState,
            includeUUIDs = includeUUIDs,
        )
    }

    override fun whereNow(uuid: String): WhereNow {
        return WhereNowEndpoint(pubnub = this, uuid = uuid)
    }

    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
        uuid: String,
    ): SetState {
        return SetStateEndpoint(
            pubnub = this,
            channels = channels,
            channelGroups = channelGroups,
            state = state,
            uuid = uuid,
            presenceData = presenceData,
        )
    }

    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any
    ): SetState {
        return SetStateEndpoint(
            pubnub = this,
            channels = channels,
            channelGroups = channelGroups,
            state = state,
            uuid = configuration.userId.value,
            presenceData = presenceData,
        )
    }

    override fun getPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        uuid: String,
    ): GetState {
        return GetStateEndpoint(pubnub = this, channels = channels, channelGroups = channelGroups, uuid = uuid)
    }

    override fun addMessageAction(
        channel: String,
        messageAction: PNMessageAction,
    ): AddMessageAction {
        return AddMessageActionEndpoint(pubnub = this, channel = channel, messageAction = messageAction)
    }

    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long,
    ): RemoveMessageAction {
        return RemoveMessageActionEndpoint(
            pubnub = this,
            channel = channel,
            messageTimetoken = messageTimetoken,
            actionTimetoken = actionTimetoken,
        )
    }

    override fun getMessageActions(
        channel: String,
        page: PNBoundedPage,
    ): GetMessageActions {
        return GetMessageActionsEndpoint(pubnub = this, channel = channel, page = page)
    }

    override fun addChannelsToChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): AddChannelChannelGroup {
        return AddChannelChannelGroupEndpoint(pubnub = this, channels = channels, channelGroup = channelGroup)
    }

    override fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        return AllChannelsChannelGroupEndpoint(pubnub = this, channelGroup = channelGroup)
    }

    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): RemoveChannelChannelGroup {
        return RemoveChannelChannelGroupEndpoint(pubnub = this, channels = channels, channelGroup = channelGroup)
    }

    override fun listAllChannelGroups(): ListAllChannelGroup {
        return ListAllChannelGroupEndpoint(this)
    }

    override fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        return DeleteChannelGroupEndpoint(pubnub = this, channelGroup = channelGroup)
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
        uuids: List<String>,
    ): Grant =
        GrantEndpoint(
            pubnub = this,
            read = read,
            write = write,
            manage = manage,
            delete = delete,
            get = get,
            update = update,
            join = join,
            ttl = ttl,
            authKeys = authKeys,
            channels = channels,
            channelGroups = channelGroups,
            uuids = uuids,
        )

    override fun grant(
        read: Boolean,
        write: Boolean,
        manage: Boolean,
        delete: Boolean,
        ttl: Int,
        authKeys: List<String>,
        channels: List<String>,
        channelGroups: List<String>,
        uuids: List<String>,
    ): Grant =
        GrantEndpoint(
            pubnub = this,
            read = read,
            write = write,
            manage = manage,
            delete = delete,
            ttl = ttl,
            authKeys = authKeys,
            channels = channels,
            channelGroups = channelGroups,
            uuids = uuids,
        )

    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>,
    ): GrantToken {
        return GrantTokenEndpoint(
            pubnub = this,
            ttl = ttl,
            meta = meta,
            authorizedUUID = authorizedUUID,
            channels = channels,
            channelGroups = channelGroups,
            uuids = uuids,
        )
    }

    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUserId: UserId?,
        spacesPermissions: List<SpacePermissions>,
        usersPermissions: List<UserPermissions>,
    ): GrantToken {
        return GrantTokenEndpoint(
            pubnub = this,
            ttl = ttl,
            meta = meta,
            authorizedUUID = authorizedUserId?.value,
            channels = spacesPermissions.map { spacePermissions -> spacePermissions.toChannelGrant() },
            channelGroups = emptyList(),
            uuids = usersPermissions.map { userPermissions -> userPermissions.toUuidGrant() },
        )
    }

    override fun revokeToken(token: String): RevokeToken {
        return RevokeTokenEndpoint(
            pubnub = this,
            token = token,
        )
    }

    override fun time(): Time {
        return TimeEndpoint(this)
    }

    override fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
    ): GetAllChannelMetadata {
        return GetAllChannelMetadataEndpoint(
            pubnub = this,
            collectionQueryParameters =
                CollectionQueryParameters(
                    limit = limit,
                    page = page,
                    filter = filter,
                    sort = sort,
                    includeCount = includeCount,
                ),
            includeQueryParam = IncludeQueryParam(includeCustom = includeCustom),
        )
    }

    override fun getChannelMetadata(
        channel: String,
        includeCustom: Boolean,
    ): GetChannelMetadata {
        return GetChannelMetadataEndpoint(
            pubnub = this,
            channel = channel,
            includeQueryParam = IncludeQueryParam(includeCustom = includeCustom),
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
        return SetChannelMetadataEndpoint(
            pubnub = this,
            channel = channel,
            name = name,
            description = description,
            custom = custom,
            includeQueryParam = IncludeQueryParam(includeCustom = includeCustom),
            type = type,
            status = status,
            ifMatchesEtag = ifMatchesEtag,
        )
    }

    override fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        return RemoveChannelMetadataEndpoint(this, channel = channel)
    }

    override fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
    ): GetAllUUIDMetadata {
        return GetAllUUIDMetadataEndpoint(
            pubnub = this,
            collectionQueryParameters =
                CollectionQueryParameters(
                    limit = limit,
                    page = page,
                    filter = filter,
                    sort = sort,
                    includeCount = includeCount,
                ),
            withInclude = IncludeQueryParam(includeCustom = includeCustom),
        )
    }

    override fun getUUIDMetadata(
        uuid: String?,
        includeCustom: Boolean,
    ): GetUUIDMetadata {
        return GetUUIDMetadataEndpoint(
            pubnub = this,
            uuid = uuid ?: configuration.userId.value,
            includeQueryParam = IncludeQueryParam(includeCustom = includeCustom),
        )
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
        status: String?,
        ifMatchesEtag: String?,
    ): SetUUIDMetadata {
        return SetUUIDMetadataEndpoint(
            pubnub = this,
            uuid = uuid,
            name = name,
            externalId = externalId,
            profileUrl = profileUrl,
            email = email,
            custom = custom,
            withInclude = IncludeQueryParam(includeCustom = includeCustom),
            type = type,
            status = status,
            ifMatchesEtag = ifMatchesEtag,
        )
    }

    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
        return RemoveUUIDMetadataEndpoint(pubnub = this, uuid = uuid)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new getMemberships(userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "getMemberships(userId = uuid, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeType = includeType))",
            "com.pubnub.api.models.consumer.objects.membership.MembershipInclude"
        )
    )
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
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: getMemberships() with uuid, includeChannelDetails and includeType parameters is deprecated. Use getMemberships(userId, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        val includeQueryParamValue = when (includeChannelDetails) {
            PNChannelDetailsLevel.CHANNEL -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeChannel = true,
                    includeChannelType = includeType,
                )
            }

            PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeChannel = true,
                    includeChannelCustom = true,
                    includeChannelType = includeType,
                )
            }

            else -> IncludeQueryParam(includeCustom = includeCustom, includeChannelType = includeType)
        }
        return GetMembershipsEndpoint(
            pubnub = this,
            uuid = uuid ?: configuration.userId.value,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = includeCount,
            ),
            includeQueryParam = includeQueryParamValue
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
        return GetMembershipsEndpoint(
            pubnub = this,
            uuid = userId ?: configuration.userId.value,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = include.includeTotalCount,
            ),
            includeQueryParam = IncludeQueryParam(
                includeCustom = include.includeCustom,
                includeType = include.includeType,
                includeStatus = include.includeStatus,
                includeChannel = include.includeChannel,
                includeChannelCustom = include.includeChannelCustom,
                includeChannelStatus = include.includeChannelStatus,
                includeChannelType = include.includeChannelType,
            )
        )
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new setMemberships(channels, userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "setMemberships(channels = channels, userId = uuid, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeType = includeType))",
            "com.pubnub.api.models.consumer.objects.membership.MembershipInclude"
        )
    )
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
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: setMemberships() with uuid, includeChannelDetails and includeType parameters is deprecated. Use setMemberships(channels, userId, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return manageMemberships(
            channelsToSet = channels,
            channelsToRemove = listOf(),
            uuid = uuid,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeChannelDetails = includeChannelDetails,
            includeType = includeType
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
    ): ManageMemberships = manageMemberships(
        channelsToSet = channels,
        channelsToRemove = listOf(),
        userId = userId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        include = include
    )

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new removeMemberships(channels, userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "removeMemberships(channels = channels, userId = uuid, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeType = includeType))",
            "com.pubnub.api.models.consumer.objects.membership.MembershipInclude"
        )
    )
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
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: removeMemberships() with uuid, includeChannelDetails and includeType parameters is deprecated. Use removeMemberships(channels, userId, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return manageMemberships(
            channelsToSet = listOf(),
            channelsToRemove = channels,
            uuid = uuid,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeChannelDetails = includeChannelDetails,
            includeType = includeType,
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
    ): ManageMemberships = manageMemberships(
        channelsToSet = listOf(),
        channelsToRemove = channels,
        userId = userId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        include = include
    )

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new manageMemberships(channelsToSet, channelsToRemove, userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "manageMemberships(channelsToSet = channelsToSet, channelsToRemove = channelsToRemove, userId = uuid, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeChannelType = includeType))",
            "com.pubnub.api.models.consumer.objects.membership.MembershipInclude"
        )
    )
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
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean,
    ): ManageMemberships {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: manageMemberships() with uuid, includeChannelDetails and includeType parameters is deprecated. Use manageMemberships(channelsToSet, channelsToRemove, userId, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        val includeQueryParamValue = when (includeChannelDetails) {
            PNChannelDetailsLevel.CHANNEL -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeChannel = true,
                    includeChannelType = includeType,
                )
            }

            PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeChannel = true,
                    includeChannelCustom = true,
                    includeChannelType = includeType,
                )
            }

            null -> IncludeQueryParam(includeCustom = includeCustom, includeChannelType = includeType)
        }
        return ManageMembershipsEndpoint(
            pubnub = this,
            channelsToSet = channelsToSet,
            channelsToRemove = channelsToRemove,
            uuid = uuid ?: configuration.userId.value,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = includeCount,
            ),
            includeQueryParam = includeQueryParamValue
        )
    }

    override fun manageMemberships(
        channelsToSet: List<ChannelMembershipInput>,
        channelsToRemove: List<String>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude
    ): ManageMemberships {
        return ManageMembershipsEndpoint(
            pubnub = this,
            channelsToSet = channelsToSet,
            channelsToRemove = channelsToRemove,
            uuid = userId ?: configuration.userId.value,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = include.includeTotalCount,
            ),
            includeQueryParam = IncludeQueryParam(
                includeCustom = include.includeCustom,
                includeType = include.includeType,
                includeStatus = include.includeStatus,
                includeChannel = include.includeChannel,
                includeChannelCustom = include.includeChannelCustom,
                includeChannelStatus = include.includeChannelStatus,
                includeChannelType = include.includeChannelType,
            ),
        )
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new getChannelMembers(channel, limit, page, filter, sort, MemberInclude(...))",
        replaceWith = ReplaceWith(
            "getChannelMembers(channel = channel, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeType))",
            "com.pubnub.api.models.consumer.objects.member.MemberInclude"
        )
    )
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
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: getChannelMembers() with includeUUIDDetails and includeType parameters is deprecated. Use getChannelMembers(channel, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        val includeQueryParamValue = when (includeUUIDDetails) {
            PNUUIDDetailsLevel.UUID -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeUser = true,
                    includeUserType = includeType,
                )
            }

            PNUUIDDetailsLevel.UUID_WITH_CUSTOM -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeUser = true,
                    includeUserCustom = true,
                    includeUserType = includeType,
                )
            }

            null -> IncludeQueryParam(includeCustom = includeCustom, includeUserType = includeType)
        }
        return GetChannelMembersEndpoint(
            pubnub = this,
            channel = channel,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = includeCount,
            ),
            includeQueryParam = includeQueryParamValue
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
        return GetChannelMembersEndpoint(
            pubnub = this,
            channel = channel,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = include.includeTotalCount,
            ),
            includeQueryParam = IncludeQueryParam(
                includeCustom = include.includeCustom,
                includeType = include.includeType,
                includeStatus = include.includeStatus,
                includeUser = include.includeUser,
                includeUserCustom = include.includeUserCustom,
                includeUserStatus = include.includeUserStatus,
                includeUserType = include.includeUserType
            )
        )
    }

    @Deprecated(
        replaceWith =
            ReplaceWith(
                "fetchMessages(channels = channels, page = PNBoundedPage(start = start, end = end," +
                    " limit = maximumPerChannel),includeMeta = includeMeta," +
                    " includeMessageActions = includeMessageActions, includeMessageType = includeMessageType)",
                "com.pubnub.api.models.consumer.PNBoundedPage",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use fetchMessages(String, PNBoundedPage, Boolean, Boolean, Boolean) instead",
    )
    override fun fetchMessages(
        channels: List<String>,
        maximumPerChannel: Int,
        start: Long?,
        end: Long?,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean,
        includeCustomMessageType: Boolean
    ): FetchMessages {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: fetchMessages() with maximumPerChannel, start, end parameters is deprecated. Use fetchMessages(channels, page, includeUUID, includeMeta, includeMessageActions, includeMessageType, includeCustomMessageType) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return fetchMessages(
            channels = channels,
            page = PNBoundedPage(start = start, end = end, limit = maximumPerChannel),
            includeUUID = true,
            includeMeta = includeMeta,
            includeMessageActions = includeMessageActions,
            includeMessageType = includeMessageType,
            includeCustomMessageType = includeCustomMessageType
        )
    }

    @Deprecated(
        replaceWith =
            ReplaceWith(
                "getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))",
                "com.pubnub.api.models.consumer.PNBoundedPage",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use getMessageActions(String, PNBoundedPage) instead",
    )
    override fun getMessageActions(
        channel: String,
        start: Long?,
        end: Long?,
        limit: Int?,
    ): GetMessageActions {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: getMessageActions() with start, end, limit parameters is deprecated. Use getMessageActions(channel, page) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))
    }

    @Deprecated(
        replaceWith =
            ReplaceWith(
                "setMemberships(channels = channels, uuid = uuid, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                    "includeChannelDetails = includeChannelDetails)",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use setMemberships instead",
    )
    override fun addMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
    ): ManageMemberships {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: addMemberships() is deprecated. Use setMemberships() instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return setMemberships(
            channels = channels,
            uuid = uuid ?: configuration.userId.value,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeChannelDetails = includeChannelDetails,
        )
    }

    @Deprecated(
        "Use getChannelMembers instead",
        replaceWith =
            ReplaceWith(
                "getChannelMembers(channel = channel, limit = limit, page = page, " +
                    "filter = filter, sort = sort, includeCount = includeCount, " +
                    "includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.WARNING,
    )
    override fun getMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): GetChannelMembers {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: getMembers() is deprecated. Use getChannelMembers() instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return getChannelMembers(
            channel = channel,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDDetails = includeUUIDDetails,
        )
    }

    @Deprecated(
        "Use setChannelMembers instead",
        replaceWith =
            ReplaceWith(
                "setChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount," +
                    " includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.WARNING,
    )
    override fun addMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): ManageChannelMembers {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: addMembers() is deprecated. Use setChannelMembers() instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return setChannelMembers(
            channel = channel,
            uuids = uuids,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDDetails = includeUUIDDetails,
        )
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new setChannelMembers(channel, users, limit, page, filter, sort, MemberInclude(...))",
        replaceWith = ReplaceWith(
            "setChannelMembers(channel = channel, users = uuids, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeType))",
            "com.pubnub.api.models.consumer.objects.member.MemberInclude"
        )
    )
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
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: setChannelMembers() with uuids, includeUUIDDetails and includeType parameters is deprecated. Use setChannelMembers(channel, users, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return manageChannelMembers(
            channel = channel,
            uuidsToSet = uuids,
            uuidsToRemove = listOf(),
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDDetails = includeUUIDDetails,
            includeUUIDType = includeType
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
    ): ManageChannelMembers = manageChannelMembers(
        channel = channel,
        userToSet = users,
        userIdsToRemove = listOf(),
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        include = include
    )

    @Deprecated(
        "Use removeChannelMembers instead",
        replaceWith =
            ReplaceWith(
                "removeChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, " +
                    "includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.WARNING,
    )
    override fun removeMembers(
        channel: String,
        uuids: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): ManageChannelMembers {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: removeMembers() is deprecated. Use removeChannelMembers() instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return removeChannelMembers(
            channel = channel,
            uuids = uuids,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDDetails = includeUUIDDetails,
        )
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new removeChannelMembers(channel, userIds, limit, page, filter, sort, MemberInclude(...))",
        replaceWith = ReplaceWith(
            "removeChannelMembers(channel = channel, userIds = uuids, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeType))",
            "com.pubnub.api.models.consumer.objects.member.MemberInclude"
        )
    )
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
        includeType: Boolean,
    ): ManageChannelMembers {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: removeChannelMembers() with uuids, includeUUIDDetails and includeType parameters is deprecated. Use removeChannelMembers(channel, userIds, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        return manageChannelMembers(
            channel = channel,
            uuidsToSet = listOf(),
            uuidsToRemove = uuids,
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDDetails = includeUUIDDetails,
            includeUUIDType = includeType,
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
    ): ManageChannelMembers = manageChannelMembers(
        channel = channel,
        userToSet = listOf(),
        userIdsToRemove = userIds,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        include = include
    )

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new manageChannelMembers(channel, userToSet, userIdsToRemove, limit, page, filter, sort, MemberInclude(...))",
        replaceWith = ReplaceWith(
            "manageChannelMembers(channel = channel, userToSet = uuidsToSet, userIdsToRemove = uuidsToRemove, limit = limit, page = page, filter = filter, sort = sort, include = com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeUUIDType))",
            "com.pubnub.api.models.consumer.objects.member.MemberInclude"
        )
    )
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
        includeUUIDDetails: PNUUIDDetailsLevel?,
        includeUUIDType: Boolean,
    ): ManageChannelMembers {
        logger.warn(
            LogMessage(
                message = LogMessageContent.Text(
                    "DEPRECATED: manageChannelMembers() with uuidsToSet, uuidsToRemove, includeUUIDDetails and includeUUIDType parameters is deprecated. Use manageChannelMembers(channel, userToSet, userIdsToRemove, limit, page, filter, sort, include) instead."
                ),
                details = "This method will be removed in a future version",
            )
        )
        val includeQueryParamValue = when (includeUUIDDetails) {
            PNUUIDDetailsLevel.UUID -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeUser = true,
                    includeUserType = includeUUIDType,
                )
            }

            PNUUIDDetailsLevel.UUID_WITH_CUSTOM -> {
                IncludeQueryParam(
                    includeCustom = includeCustom,
                    includeUser = true,
                    includeUserCustom = true,
                    includeUserType = includeUUIDType,
                )
            }

            null -> IncludeQueryParam(includeCustom = includeCustom, includeUserType = includeUUIDType)
        }
        return ManageChannelMembersEndpoint(
            pubnub = this,
            channel = channel,
            userToSet = uuidsToSet,
            userIdsRemove = uuidsToRemove,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = includeCount,
            ),
            includeQueryParam = includeQueryParamValue
        )
    }

    override fun manageChannelMembers(
        channel: String,
        userToSet: Collection<MemberInput>,
        userIdsToRemove: Collection<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): ManageChannelMembers {
        return ManageChannelMembersEndpoint(
            pubnub = this,
            channel = channel,
            userToSet = userToSet,
            userIdsRemove = userIdsToRemove,
            collectionQueryParameters = CollectionQueryParameters(
                limit = limit,
                page = page,
                filter = filter,
                sort = sort,
                includeCount = include.includeTotalCount,
            ),
            includeQueryParam = IncludeQueryParam(
                includeCustom = include.includeCustom,
                includeType = include.includeType,
                includeStatus = include.includeStatus,
                includeUser = include.includeUser,
                includeUserCustom = include.includeUserCustom,
                includeUserStatus = include.includeUserStatus,
                includeUserType = include.includeUserType
            )
        )
    }

    override fun sendFile(
        channel: String,
        fileName: String,
        inputStream: InputStream,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?,
        customMessageType: String?,
    ): SendFile {
        val cryptoModule =
            if (cipherKey != null) {
                createCryptoModuleWithLogConfig(CryptoModule.createLegacyCryptoModule(cipherKey))
            } else {
                cryptoModuleWithLogConfig
            }
        return SendFileEndpoint(
            channel = channel,
            fileName = fileName,
            inputStream = inputStream,
            message = message,
            meta = meta,
            ttl = ttl,
            shouldStore = shouldStore,
            customMessageType = customMessageType,
            executorService =
                retrofitManager.getTransactionClientExecutorService()
                    ?: Executors.newSingleThreadExecutor(),
            fileMessagePublishRetryLimit = configuration.fileMessagePublishRetryLimit,
            generateUploadUrlFactory = GenerateUploadUrlEndpoint.Factory(this),
            publishFileMessageFactory = PublishFileMessageEndpoint.Factory(this),
            sendFileToS3Factory = UploadFileEndpoint.Factory(this),
            cryptoModule = cryptoModule,
        )
    }

    override fun listFiles(
        channel: String,
        limit: Int?,
        next: PNPage.PNNext?,
    ): ListFiles {
        return ListFilesEndpoint(
            pubNub = this,
            channel = channel,
            limit = limit,
            next = next,
        )
    }

    override fun getFileUrl(
        channel: String,
        fileName: String,
        fileId: String,
    ): GetFileUrl {
        return GetFileUrlEndpoint(
            pubNub = this,
            channel = channel,
            fileName = fileName,
            fileId = fileId,
        )
    }

    override fun downloadFile(
        channel: String,
        fileName: String,
        fileId: String,
        cipherKey: String?,
    ): DownloadFile {
        val cryptoModule =
            if (cipherKey != null) {
                createCryptoModuleWithLogConfig(CryptoModule.createLegacyCryptoModule(cipherKey))
            } else {
                cryptoModuleWithLogConfig
            }
        return DownloadFileEndpoint(
            pubNub = this,
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            cryptoModule = cryptoModule,
        )
    }

    override fun deleteFile(
        channel: String,
        fileName: String,
        fileId: String,
    ): DeleteFile {
        return DeleteFileEndpoint(
            pubNub = this,
            channel = channel,
            fileName = fileName,
            fileId = fileId,
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
        customMessageType: String?,
    ): PublishFileMessage {
        return PublishFileMessageEndpoint(
            pubNub = this,
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

    override fun getSubscribedChannels() = subscribe.getSubscribedChannels()

    override fun getSubscribedChannelGroups() = subscribe.getSubscribedChannelGroups()

    override fun presence(
        channels: List<String>,
        channelGroups: List<String>,
        connected: Boolean,
    ) = presence.presence(
        channels = channels.toSet(),
        channelGroups = channelGroups.toSet(),
        connected = connected,
    )

    private fun getCryptoModuleOrThrow(cipherKey: String? = null): CryptoModule {
        return cipherKey?.let { cipherKeyNotNull ->
            createCryptoModuleWithLogConfig(CryptoModule.createLegacyCryptoModule(cipherKeyNotNull))
        }
            ?: cryptoModuleWithLogConfig ?: throw PubNubException("Crypto module is not initialized")
    }

    @Throws(PubNubException::class)
    fun decrypt(
        inputString: String,
        cryptoModule: CryptoModule? = null,
    ): String = getCryptoModuleOrThrow(cryptoModule).decryptString(inputString)

    @Throws(PubNubException::class)
    override fun decrypt(inputString: String): String = decrypt(inputString, cipherKey = null)

    override fun decrypt(
        inputString: String,
        cipherKey: String?,
    ): String = decrypt(inputString, getCryptoModuleOrThrow(cipherKey))

    override fun decryptInputStream(
        inputStream: InputStream,
        cipherKey: String?,
    ): InputStream = decryptInputStream(inputStream, getCryptoModuleOrThrow(cipherKey))

    private fun decryptInputStream(
        inputStream: InputStream,
        cryptoModule: CryptoModule? = null,
    ): InputStream = getCryptoModuleOrThrow(cryptoModule).decryptStream(inputStream)

    override fun encrypt(
        inputString: String,
        cipherKey: String?,
    ): String = encrypt(inputString, getCryptoModuleOrThrow(cipherKey))

    @Throws(PubNubException::class)
    private fun encrypt(
        inputString: String,
        cryptoModule: CryptoModule? = null,
    ): String = getCryptoModuleOrThrow(cryptoModule).encryptString(inputString)

    override fun encryptInputStream(
        inputStream: InputStream,
        cipherKey: String?,
    ): InputStream = encryptInputStream(inputStream, getCryptoModuleOrThrow(cipherKey))

    @Throws(PubNubException::class)
    private fun encryptInputStream(
        inputStream: InputStream,
        cryptoModule: CryptoModule? = null,
    ): InputStream = getCryptoModuleOrThrow(cryptoModule).encryptStream(inputStream)

    private fun subscribeInternal(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        withPresence: Boolean = false,
        withTimetoken: Long = 0L,
    ) {
        subscribe.subscribe(channels.toSet(), channelGroups.toSet(), withPresence, withTimetoken)
        if (!configuration.managePresenceListManually) {
            presence.joined(
                channels.filterNot { it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet(),
                channelGroups.filterNot { it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet(),
            )
        }
    }

    private fun unsubscribeInternal(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
    ) {
        val channelSetWithoutPresence = channels.filter { !it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet()
        val groupSetWithoutPresence = channelGroups.filter { !it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet()
        subscribe.unsubscribe(channelSetWithoutPresence, groupSetWithoutPresence)
        if (!configuration.managePresenceListManually) {
            presence.left(channelSetWithoutPresence, groupSetWithoutPresence)
        }
    }

    override fun reconnect(timetoken: Long) {
        subscribe.reconnect(timetoken)
        presence.reconnect()
    }

    override fun disconnect() {
        subscribe.disconnect()
        presence.disconnect()
    }

    override fun destroy() {
        subscribe.destroy()
        presence.destroy()

        retrofitManager.destroy()
        executorService.shutdown()
    }

    override fun forceDestroy() {
        subscribe.destroy()
        presence.destroy()

        retrofitManager.destroy(true)
        executorService.shutdownNow()
    }

    override fun parseToken(token: String): PNToken {
        return tokenParser.unwrapToken(token)
    }

    override fun setToken(token: String?) {
        return tokenManager.setToken(token)
    }

    override fun getToken(): String? {
        return tokenManager.getToken()
    }

    // internal
    private val lockChannelsAndGroups = Any()
    private val channelSubscriptions = mutableMapOf<ChannelName, MutableSet<Subscription>>()
    private val channelGroupSubscriptions = mutableMapOf<ChannelGroupName, MutableSet<Subscription>>()

    internal fun subscribe(
        vararg subscriptions: SubscriptionInternal,
        cursor: SubscriptionCursor,
    ) {
        synchronized(lockChannelsAndGroups) {
            val channelsToSubscribe = mutableSetOf<ChannelName>()
            subscriptions.forEach { subscription ->
                subscription.channels.forEach { channelName ->
                    channelSubscriptions.computeIfAbsent(channelName) { mutableSetOf() }
                        .also { set -> set.add(subscription) }
                    channelsToSubscribe.add(channelName)
                }
            }
            val groupsToSubscribe = mutableSetOf<ChannelGroupName>()
            subscriptions.forEach { subscription ->
                subscription.channelGroups.forEach { channelGroupName ->
                    channelGroupSubscriptions.computeIfAbsent(channelGroupName) { mutableSetOf() }
                        .also { set -> set.add(subscription) }
                    groupsToSubscribe.add(channelGroupName)
                }
            }

            val (channelsWithPresence, channelsNoPresence) =
                channelsToSubscribe.filter { !it.isPresence }
                    .partition {
                        channelsToSubscribe.contains(it.withPresence)
                    }
            val (groupsWithPresence, groupsNoPresence) =
                groupsToSubscribe.filter { !it.isPresence }.partition {
                    groupsToSubscribe.contains(it.withPresence)
                }
            if (channelsWithPresence.isNotEmpty() || groupsWithPresence.isNotEmpty()) {
                subscribeInternal(
                    channels = channelsWithPresence.map(ChannelName::id),
                    channelGroups = groupsWithPresence.map(ChannelGroupName::id),
                    withPresence = true,
                    withTimetoken = cursor.timetoken,
                )
            }
            if (channelsNoPresence.isNotEmpty() || groupsNoPresence.isNotEmpty()) {
                subscribeInternal(
                    channels = channelsNoPresence.map(ChannelName::id),
                    channelGroups = groupsNoPresence.map(ChannelGroupName::id),
                    withPresence = false,
                    withTimetoken = cursor.timetoken,
                )
            }
        }
    }

    internal fun unsubscribe(vararg subscriptions: SubscriptionInternal) {
        synchronized(lockChannelsAndGroups) {
            val channelsToUnsubscribe = mutableSetOf<ChannelName>()
            subscriptions.forEach { subscription ->
                subscription.channels.forEach { channelName ->
                    val set = channelSubscriptions[channelName]
                    set?.remove(subscription)
                    if (set != null && set.isEmpty()) { // there were mappings but there none now
                        channelsToUnsubscribe += channelName
                        channelSubscriptions.remove(channelName)
                    }
                }
            }

            val groupsToUnsubscribe = mutableSetOf<ChannelGroupName>()
            subscriptions.forEach { subscription ->
                subscription.channelGroups.forEach { channelGroupName ->
                    val set = channelGroupSubscriptions[channelGroupName]
                    set?.remove(subscription)
                    if (set != null && set.isEmpty()) {
                        groupsToUnsubscribe += channelGroupName
                        channelGroupSubscriptions.remove(channelGroupName)
                    }
                }
            }
            if (channelsToUnsubscribe.isNotEmpty() || groupsToUnsubscribe.isNotEmpty()) {
                unsubscribeInternal(
                    channels = channelsToUnsubscribe.map(ChannelName::id),
                    channelGroups = groupsToUnsubscribe.map(ChannelGroupName::id),
                )
            }
        }
    }

    private val channelSubscriptionMap = mutableMapOf<ChannelName, SubscriptionImpl>()
    private val channelGroupSubscriptionMap = mutableMapOf<ChannelGroupName, SubscriptionImpl>()

    //region Subscribe
    @Synchronized
    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long,
    ) {
        val toSubscribe = mutableSetOf<SubscriptionImpl>()
        channels.filter { it.isNotEmpty() }.map { ChannelName(it) }.forEach { channelName ->
            // if we are adding a NEW subscriptions in this step, this var will contain it:
            var subscription: SubscriptionImpl? = null
            channelSubscriptionMap.computeIfAbsent(channelName) { newChannelName ->
                val channel =
                    ChannelImpl(
                        this,
                        newChannelName
                    )
                val options =
                    if (withPresence) {
                        SubscriptionOptions.receivePresenceEvents()
                    } else {
                        EmptyOptions
                    }
                channel.subscription(options).also { sub ->
                    toSubscribe.add(sub)
                    subscription = sub
                }
            }
            // make sure we are also subscribed and tracking the -pnpres channel if withPresence==true
            if (withPresence) {
                channelSubscriptionMap.computeIfAbsent(channelName.withPresence) { presenceChannelName ->
                    // this will either be the subscriptions we just created in the previous step,
                    // or if we were already subscribed to the channel WITHOUT presence, we need to create a new one
                    subscription ?: ChannelImpl(
                        this,
                        presenceChannelName
                    ).subscription().also { sub ->
                        toSubscribe.add(sub)
                    }
                }
            }
        }
        channelGroups.filter { it.isNotEmpty() }.map { ChannelGroupName(it) }.forEach { channelGroupName ->
            var subscription: SubscriptionImpl? = null

            channelGroupSubscriptionMap.computeIfAbsent(channelGroupName) { newChannelGroupName ->
                val channelGroup = ChannelGroupImpl(this, newChannelGroupName)
                val options =
                    if (withPresence) {
                        SubscriptionOptions.receivePresenceEvents()
                    } else {
                        EmptyOptions
                    }
                channelGroup.subscription(options).also { sub ->
                    toSubscribe.add(sub)
                    subscription = sub
                }
            }
            // make sure we are also subscribed and tracking the -pnpres channel if withPresence==true
            if (withPresence) {
                channelGroupSubscriptionMap.computeIfAbsent(channelGroupName.withPresence) { presenceGroupName ->
                    // this will either be the subscriptions we just created in the previous step,
                    // or if we were already subscribed to the channel WITHOUT presence, we need to create a new one
                    subscription ?: ChannelGroupImpl(this, presenceGroupName)
                        .subscription().also { sub ->
                            toSubscribe.add(sub)
                        }
                }
            }
        }

        // actually subscribe to all subscriptions created in this function and added to the set
        subscribe(*toSubscribe.toTypedArray(), cursor = SubscriptionCursor(withTimetoken))
    }

    @Synchronized
    override fun unsubscribe(
        channels: List<String>,
        channelGroups: List<String>,
    ) {
        val toUnsubscribe: MutableSet<SubscriptionImpl> = mutableSetOf()
        channels.filter { it.isNotEmpty() }.map { ChannelName(it) }.forEach { channelName ->
            channelSubscriptionMap.remove(channelName)?.let { sub ->
                toUnsubscribe.add(sub)
            }
            channelSubscriptionMap.remove(channelName.withPresence)?.let { sub ->
                toUnsubscribe.add(sub)
            }
        }
        channelGroups.filter { it.isNotEmpty() }.map { ChannelGroupName(it) }.forEach { groupName ->
            channelGroupSubscriptionMap.remove(groupName)?.let { sub ->
                toUnsubscribe.add(sub)
            }
            channelGroupSubscriptionMap.remove(groupName.withPresence)?.let { sub ->
                toUnsubscribe.add(sub)
            }
        }
        unsubscribe(*toUnsubscribe.toTypedArray())
    }

    @Synchronized
    override fun unsubscribeAll() {
        synchronized(lockChannelsAndGroups) {
            channelSubscriptions.clear()
            channelGroupSubscriptions.clear()
            subscribe.unsubscribeAll()
            presence.leftAll()
        }
    }

    @Throws(PubNubException::class)
    private fun getCryptoModuleOrThrow(cryptoModule: CryptoModule? = null): CryptoModule {
        return cryptoModule?.let { createCryptoModuleWithLogConfig(it) } ?: cryptoModuleWithLogConfig
            ?: throw PubNubException("Crypto module is not initialized")
    }

    private fun createCryptoModuleWithLogConfig(cryptoModule: CryptoModule): CryptoModule {
        return if (cryptoModule is CryptoModuleImpl) {
            CryptoModuleImpl(
                primaryCryptor = cryptoModule.primaryCryptor,
                cryptorsForDecryptionOnly = cryptoModule.cryptorsForDecryptionOnly,
                logConfig = logConfig
            )
        } else {
            // For custom implementations, return the original instance
            cryptoModule
        }
    }
}
