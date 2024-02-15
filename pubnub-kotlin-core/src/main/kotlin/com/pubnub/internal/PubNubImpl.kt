package com.pubnub.internal

import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.decryptString
import com.pubnub.api.crypto.encryptString
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.endpoints.DeleteMessages
import com.pubnub.internal.endpoints.FetchMessages
import com.pubnub.internal.endpoints.History
import com.pubnub.internal.endpoints.MessageCounts
import com.pubnub.internal.endpoints.Time
import com.pubnub.internal.endpoints.access.Grant
import com.pubnub.internal.endpoints.access.GrantToken
import com.pubnub.internal.endpoints.access.RevokeToken
import com.pubnub.internal.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.internal.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.internal.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.internal.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.internal.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.internal.endpoints.files.DeleteFile
import com.pubnub.internal.endpoints.files.DownloadFile
import com.pubnub.internal.endpoints.files.GenerateUploadUrl
import com.pubnub.internal.endpoints.files.GetFileUrl
import com.pubnub.internal.endpoints.files.ListFiles
import com.pubnub.internal.endpoints.files.PublishFileMessage
import com.pubnub.internal.endpoints.files.SendFile
import com.pubnub.internal.endpoints.files.UploadFile
import com.pubnub.internal.endpoints.message_actions.AddMessageAction
import com.pubnub.internal.endpoints.message_actions.GetMessageActions
import com.pubnub.internal.endpoints.message_actions.RemoveMessageAction
import com.pubnub.internal.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.RemoveChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.endpoints.objects.member.GetChannelMembers
import com.pubnub.internal.endpoints.objects.member.ManageChannelMembers
import com.pubnub.internal.endpoints.objects.membership.GetMemberships
import com.pubnub.internal.endpoints.objects.membership.ManageMemberships
import com.pubnub.internal.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.SetUUIDMetadata
import com.pubnub.internal.endpoints.presence.GetState
import com.pubnub.internal.endpoints.presence.HereNow
import com.pubnub.internal.endpoints.presence.SetState
import com.pubnub.internal.endpoints.presence.WhereNow
import com.pubnub.internal.endpoints.pubsub.Publish
import com.pubnub.internal.endpoints.pubsub.Signal
import com.pubnub.internal.endpoints.push.AddChannelsToPush
import com.pubnub.internal.endpoints.push.ListPushProvisions
import com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.internal.endpoints.push.RemoveChannelsFromPush
import com.pubnub.internal.managers.BasePathManager
import com.pubnub.internal.managers.DuplicationManager
import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.managers.MapperManager
import com.pubnub.internal.managers.PublishSequenceManager
import com.pubnub.internal.managers.RetrofitManager
import com.pubnub.internal.managers.TelemetryManager
import com.pubnub.internal.managers.TokenManager
import com.pubnub.internal.managers.TokenParser
import com.pubnub.internal.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.internal.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.internal.models.consumer.access_manager.sum.toChannelGrant
import com.pubnub.internal.models.consumer.access_manager.sum.toUuidGrant
import com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.internal.models.consumer.objects.PNKey
import com.pubnub.internal.models.consumer.objects.PNMemberKey
import com.pubnub.internal.models.consumer.objects.PNMembershipKey
import com.pubnub.internal.models.consumer.objects.PNSortKey
import com.pubnub.internal.models.consumer.objects.member.MemberInput
import com.pubnub.internal.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.internal.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.internal.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.internal.presence.Presence
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.internal.presence.eventengine.effect.effectprovider.HeartbeatProviderImpl
import com.pubnub.internal.presence.eventengine.effect.effectprovider.LeaveProviderImpl
import com.pubnub.internal.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.internal.subscribe.Subscribe
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.internal.v2.entities.ChannelGroupImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelImpl
import com.pubnub.internal.v2.entities.ChannelMetadataImpl
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.entities.UserMetadataImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.subscription.SubscriptionSetImpl
import com.pubnub.internal.workers.SubscribeMessageProcessor
import java.io.InputStream
import java.util.Date
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration.Companion.seconds

class PubNubImpl internal constructor(
    private val basePubNub: BasePubNub,
    val configuration: PNConfiguration,
    listenerManager: ListenerManager,
    eventEnginesConf: EventEnginesConf = EventEnginesConf()
) {

    internal val cryptoModule: CryptoModule?
        get() = configuration.cryptoModule

    //region Managers

    /**
     * Manage and parse JSON
     */
    val mapper = MapperManager()

    private val numberOfThreadsInPool = Integer.min(Runtime.getRuntime().availableProcessors(), 8)
    internal val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(numberOfThreadsInPool)

    private val basePathManager = BasePathManager(configuration)
    internal val retrofitManager = RetrofitManager(this)
    internal val publishSequenceManager = PublishSequenceManager(BasePubNub.MAX_SEQUENCE)
    internal val telemetryManager = TelemetryManager()
    internal val tokenManager: TokenManager = TokenManager()
    private val tokenParser: TokenParser = TokenParser()

    private val presenceData = PresenceData()
    private val subscribe = Subscribe.create(
        this,
        listenerManager,
        configuration.retryConfiguration,
        eventEnginesConf,
        SubscribeMessageProcessor(this, DuplicationManager(configuration)),
        presenceData,
        configuration.maintainPresenceState,
        executorService
    )

    private val presence = Presence.create(
        heartbeatProvider = HeartbeatProviderImpl(this),
        leaveProvider = LeaveProviderImpl(this),
        heartbeatInterval = configuration.heartbeatInterval.seconds,
        retryConfiguration = configuration.retryConfiguration,
        suppressLeaveEvents = configuration.suppressLeaveEvents,
        heartbeatNotificationOptions = configuration.heartbeatNotificationOptions,
        listenerManager = listenerManager,
        eventEngineConf = eventEnginesConf.presence,
        presenceData = presenceData,
        sendStateWithHeartbeat = configuration.maintainPresenceState,
        executorService = executorService
    )

    //endregion

    /**
     * The current version of the Kotlin SDK.
     */
    val version = BasePubNub.SDK_VERSION

    /**
     * Unique id of this PubNub instance.
     *
     * @see [PNConfiguration.includeInstanceIdentifier]
     */
    val instanceId = UUID.randomUUID().toString()

    //region Internal
    internal fun baseUrl() = basePathManager.basePath()
    internal fun requestId() = UUID.randomUUID().toString()
    internal fun timestamp() = (Date().time / BasePubNub.TIMESTAMP_DIVIDER).toInt()
    //endregion

    //region Publish
    /**
     * Send a message to all subscribers of a channel.
     *
     * To publish a message you must first specify a valid [PNConfiguration.publishKey].
     * A successfully published message is replicated across the PubNub Real-Time Network and sent
     * simultaneously to all subscribed clients on a channel.
     *
     * Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting
     * [PNConfiguration.secure] to `true` during initialization.
     *
     * **Publish Anytime**
     *
     *
     * It is not required to be subscribed to a channel in order to publish to that channel.
     *
     * **Message Data:**
     *
     *
     * The message argument can contain any JSON serializable data, including: Objects, Arrays, Integers and Strings.
     * Data should not contain special Java/Kotlin classes or functions as these will not serialize.
     * String content can include any single-byte or multi-byte UTF-8 character.
     *
     *
     * @param message The payload.
     *                **Warning:** It is important to note that you should not serialize JSON
     *                when sending signals/messages via PubNub.
     *                Why? Because the serialization is done for you automatically.
     *                Instead just pass the full object as the message payload.
     *                PubNub takes care of everything for you.
     * @param channel Destination of the message.
     * @param meta Metadata object which can be used with the filtering ability.
     * @param shouldStore Store in history.
     *                    If not specified, then the history configuration of the key is used.
     * @param usePost Use HTTP POST to publish. Default is `false`
     * @param replicate Replicate the message. Is set to `true` by default.
     * @param ttl Set a per message time to live in storage.
     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
     *              with no expiry time.
     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
     *              the message is stored with an expiry time of `X` hours.
     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
     *            - If ttl isn't specified, then expiration of the message defaults
     *              back to the expiry value for the key.
     */
    fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ) = Publish(
        pubnub = this,
        channel = channel,
        message = message,
        meta = meta,
        shouldStore = shouldStore,
        usePost = usePost,
        replicate = replicate,
        ttl = ttl
    )

    /**
     * Send a message to PubNub Functions Event Handlers.
     *
     * These messages will go directly to any Event Handlers registered on the channel that you fire to
     * and will trigger their execution. The content of the fired request will be available for processing
     * within the Event Handler.
     *
     * The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel.
     * The message is also not stored in history.
     *
     *
     * @param message The payload.
     *                **Warning:** It is important to note that you should not serialize JSON
     *                when sending signals/messages via PubNub.
     *                Why? Because the serialization is done for you automatically.
     *                Instead just pass the full object as the message payload.
     *                PubNub takes care of everything for you.
     * @param channel Destination of the message.
     * @param meta Metadata object which can be used with the filtering ability.
     *             If not specified, then the history configuration of the key is used.
     * @param usePost Use HTTP POST to publish. Default is `false`
     * @param ttl Set a per message time to live in storage.
     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
     *              with no expiry time.
     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
     *              the message is stored with an expiry time of `X` hours.
     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
     *            - If ttl isn't specified, then expiration of the message defaults
     *              back to the expiry value for the key.
     */
    fun fire(
        channel: String,
        message: Any,
        meta: Any? = null,
        usePost: Boolean = false,
        ttl: Int? = null
    ) = publish(
        channel = channel,
        message = message,
        meta = meta,
        shouldStore = false,
        usePost = usePost,
        replicate = false,
        ttl = ttl
    )

    /**
     * Send a signal to all subscribers of a channel.
     *
     * By default, signals are limited to a message payload size of 30 bytes.
     * This limit applies only to the payload, and not to the URI or headers.
     * If you require a larger payload size, please [contact support](mailto:support@pubnub.com).
     *
     * @param channel The channel which the signal will be sent to.
     * @param message The payload which will be serialized and sent.
     */
    fun signal(
        channel: String,
        message: Any
    ) = Signal(pubnub = this, channel = channel, message = message)
    //endregion

    private val channelSubscriptionMap = mutableMapOf<ChannelName, SubscriptionImpl>()
    private val channelGroupSubscriptionMap = mutableMapOf<ChannelGroupName, SubscriptionImpl>()

    //region Subscribe
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
    @Synchronized
    fun subscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        withPresence: Boolean = false,
        withTimetoken: Long = 0L
    ) {
        val toSubscribe = mutableSetOf<SubscriptionImpl>()
        channels.map { ChannelName(it) }.forEach { channelName ->
            // if we are adding a NEW subscription in this step, this var will contain it:
            var subscription: SubscriptionImpl? = null
            channelSubscriptionMap.computeIfAbsent(channelName) { newChannelName ->
                val channel = ChannelImpl(basePubNub, newChannelName)
                val options = if (withPresence) {
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
                    // this will either be the subscription we just created in the previous step,
                    // or if we were already subscribed to the channel WITHOUT presence, we need to create a new one
                    subscription ?: ChannelImpl(basePubNub, presenceChannelName).subscription().also { sub ->
                        toSubscribe.add(sub)
                    }
                }
            }
        }
        channelGroups.map { ChannelGroupName(it) }.forEach { channelGroupName ->
            var subscription: SubscriptionImpl? = null

            channelGroupSubscriptionMap.computeIfAbsent(channelGroupName) { newChannelGroupName ->
                val channelGroup = ChannelGroupImpl(basePubNub, newChannelGroupName)
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
                    // this will either be the subscription we just created in the previous step,
                    // or if we were already subscribed to the channel WITHOUT presence, we need to create a new one
                    subscription ?: ChannelGroupImpl(basePubNub, presenceGroupName).subscription().also { sub ->
                        toSubscribe.add(sub)
                    }
                }
            }
        }

        // actually subscribe to all subscriptions created in this function and added to the set
        subscribe(*toSubscribe.toTypedArray(), cursor = SubscriptionCursor(withTimetoken))
    }

    private fun subscribeInternal(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        withPresence: Boolean = false,
        withTimetoken: Long = 0L
    ) {
        subscribe.subscribe(channels.toSet(), channelGroups.toSet(), withPresence, withTimetoken)
        presence.joined(
            channels.filterNot { it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet(),
            channelGroups.filterNot { it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet()
        )
    }

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
    @Synchronized
    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) {
        val toUnsubscribe: MutableSet<SubscriptionImpl> = mutableSetOf()
        channels.map { ChannelName(it) }.forEach { channelName ->
            channelSubscriptionMap.remove(channelName)?.let { sub ->
                toUnsubscribe.add(sub)
            }
            channelSubscriptionMap.remove(channelName.withPresence)?.let { sub ->
                toUnsubscribe.add(sub)
            }
        }
        channelGroups.map { ChannelGroupName(it) }.forEach { groupName ->
            channelGroupSubscriptionMap.remove(groupName)?.let { sub ->
                toUnsubscribe.add(sub)
            }
            channelGroupSubscriptionMap.remove(groupName.withPresence)?.let { sub ->
                toUnsubscribe.add(sub)
            }
        }
        unsubscribe(*toUnsubscribe.toTypedArray())
    }

    private fun unsubscribeInternal(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) {
        val channelSetWithoutPresence = channels.filter { !it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet()
        val groupSetWithoutPresence = channelGroups.filter { !it.endsWith(PRESENCE_CHANNEL_SUFFIX) }.toSet()
        subscribe.unsubscribe(channelSetWithoutPresence, groupSetWithoutPresence)
        presence.left(channelSetWithoutPresence, groupSetWithoutPresence)
    }

    /**
     * Unsubscribe from all channels and all channel groups
     */
    @Synchronized
    fun unsubscribeAll() {
        synchronized(channelSubscriptions) {
            synchronized(channelGroupSubscriptions) {
                channelSubscriptions.clear()
                channelGroupSubscriptions.clear()
                subscribe.unsubscribeAll()
                presence.leftAll()
            }
        }
    }

    /**
     * Queries the local subscribe loop for channels currently in the mix.
     *
     * @return A list of channels the client is currently subscribed to.
     */
    fun getSubscribedChannels() = subscribe.getSubscribedChannels()

    /**
     * Queries the local subscribe loop for channel groups currently in the mix.
     *
     * @return A list of channel groups the client is currently subscribed to.
     */
    fun getSubscribedChannelGroups() = subscribe.getSubscribedChannelGroups()

    //endregion

    //region MobilePush
    /**
     * Enable push notifications on provided set of channels.
     *
     * @param pushType Accepted values: FCM, APNS, MPNS, APNS2.
     *                 @see [PNPushType]
     * @param channels Channels to add push notifications to.
     * @param deviceId The device ID (token) to associate with push notifications.
     * @param environment Environment within which device should manage list of channels with enabled notifications
     *                    (works only if [pushType] set to [PNPushType.APNS2]).
     * @param topic Notifications topic name (usually it is bundle identifier of application for Apple platform).
     *              Required only if pushType set to [PNPushType.APNS2].
     */
    fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ) = AddChannelsToPush(
        pubnub = this,
        pushType = pushType,
        channels = channels,
        deviceId = deviceId,
        topic = topic,
        environment = environment
    )

    /**
     * Request a list of all channels on which push notifications have been enabled using specified [ListPushProvisions.deviceId].
     *
     * @param pushType Accepted values: FCM, APNS, MPNS, APNS2. @see [PNPushType]
     * @param deviceId The device ID (token) to associate with push notifications.
     * @param environment Environment within which device should manage list of channels with enabled notifications
     *                    (works only if [pushType] set to [PNPushType.APNS2]).
     * @param topic Notifications topic name (usually it is bundle identifier of application for Apple platform).
     *              Required only if pushType set to [PNPushType.APNS2].
     */
    fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ) = ListPushProvisions(
        pubnub = this,
        pushType = pushType,
        deviceId = deviceId,
        topic = topic,
        environment = environment
    )

    /**
     * Disable push notifications on provided set of channels.
     *
     * @param pushType Accepted values: FCM, APNS, MPNS, APNS2. @see [PNPushType]
     * @param channels Channels to remove push notifications from.
     * @param deviceId The device ID (token) associated with push notifications.
     * @param environment Environment within which device should manage list of channels with enabled notifications
     *                    (works only if [pushType] set to [PNPushType.APNS2]).
     * @param topic Notifications topic name (usually it is bundle identifier of application for Apple platform).
     *              Required only if pushType set to [PNPushType.APNS2].
     */
    fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ) = RemoveChannelsFromPush(
        pubnub = this,
        pushType = pushType,
        channels = channels,
        deviceId = deviceId,
        topic = topic,
        environment = environment
    )

    /**
     * Disable push notifications from all channels registered with the specified [RemoveAllPushChannelsForDevice.deviceId].
     *
     * @param pushType Accepted values: FCM, APNS, MPNS, APNS2. @see [PNPushType]
     * @param deviceId The device ID (token) to associate with push notifications.
     * @param environment Environment within which device should manage list of channels with enabled notifications
     *                    (works only if [pushType] set to [PNPushType.APNS2]).
     * @param topic Notifications topic name (usually it is bundle identifier of application for Apple platform).
     *              Required only if pushType set to [PNPushType.APNS2].
     */
    fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ) = RemoveAllPushChannelsForDevice(
        pubnub = this,
        pushType = pushType,
        deviceId = deviceId,
        topic = topic,
        environment = environment
    )
    //endregion

    //region StoragePlayback
    /**
     * Fetch historical messages of a channel.
     *
     * It is possible to control how messages are returned and in what order, for example you can:
     * - Search for messages starting on the newest end of the timeline (default behavior - `reverse = false`)
     * - Search for messages from the oldest end of the timeline by setting `reverse` to `true`.
     * - Page through results by providing a `start` OR `end` timetoken.
     * - Retrieve a slice of the time line by providing both a `start` AND `end` timetoken.
     * - Limit the number of messages to a specific quantity using the `count` parameter.
     *
     * **Start & End parameter usage clarity:**
     * - If only the `start` parameter is specified (without `end`),
     * you will receive messages that are older than and up to that `start` timetoken value.
     * - If only the `end` parameter is specified (without `start`)
     * you will receive messages that match that end timetoken value and newer.
     * - Specifying values for both start and end parameters
     * will return messages between those timetoken values (inclusive on the `end` value)
     * - Keep in mind that you will still receive a maximum of 100 messages
     * even if there are more messages that meet the timetoken values.
     * Iterative calls to history adjusting the start timetoken is necessary to page through the full set of results
     * if more than 100 messages meet the timetoken values.
     *
     * @param channel Channel to return history messages from.
     * @param start Timetoken delimiting the start of time slice (exclusive) to pull messages from.
     * @param end Timetoken delimiting the end of time slice (inclusive) to pull messages from.
     * @param count Specifies the number of historical messages to return.
     *              Default and maximum value is `100`.
     * @param reverse Whether to traverse the time ine in reverse starting with the oldest message first.
     *                Default is `false`.
     * @param includeTimetoken Whether to include message timetokens in the response.
     *                         Defaults to `false`.
     * @param includeMeta Whether to include message metadata in response.
     *                    Defaults to `false`.
     */
    fun history(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        count: Int = History.MAX_COUNT,
        reverse: Boolean = false,
        includeTimetoken: Boolean = false,
        includeMeta: Boolean = false
    ) = History(
        pubnub = this,
        channel = channel,
        start = start,
        end = end,
        count = count,
        reverse = reverse,
        includeTimetoken = includeTimetoken,
        includeMeta = includeMeta
    )

    /**
     * Fetch historical messages from multiple channels.
     * The `includeMessageActions` flag also allows you to fetch message actions along with the messages.
     *
     * It's possible to control how messages are returned and in what order. For example, you can:
     * - Search for messages starting on the newest end of the timeline.
     * - Search for messages from the oldest end of the timeline.
     * - Page through results by providing a `start` OR `end` time token.
     * - Retrieve a slice of the time line by providing both a `start` AND `end` time token.
     * - Limit the number of messages to a specific quantity using the `count` parameter.
     * - Batch history returns up to 25 messages per channel, on a maximum of 500 channels.
     * Use the start and end timestamps to page through the next batch of messages.
     *
     * **Start & End parameter usage clarity:**
     * - If you specify only the `start` parameter (without `end`),
     * you will receive messages that are older than and up to that `start` timetoken.
     * - If you specify only the `end` parameter (without `start`),
     * you will receive messages from that `end` timetoken and newer.
     * - Specify values for both `start` and `end` parameters to retrieve messages between those timetokens
     * (inclusive of the `end` value).
     * - Keep in mind that you will still receive a maximum of 25 messages
     * even if there are more messages that meet the timetoken values.
     * - Iterative calls to history adjusting the start timetoken is necessary to page through the full set of results
     * if more than 25 messages meet the timetoken values.
     *
     * @param channels Channels to return history messages from.
     * @param maximumPerChannel Specifies the number of historical messages to return per channel.
     *                          If [includeMessageActions] is `false`, then `1` is the default (and maximum) value.
     *                          Otherwise it's `25`.
     * @param start Timetoken delimiting the start of time slice (exclusive) to pull messages from.
     * @param end Time token delimiting the end of time slice (inclusive) to pull messages from.
     * @param includeMeta Whether to include message metadata in response.
     *                    Defaults to `false`.
     * @param includeMessageActions Whether to include message actions in response.
     *                              Defaults to `false`.
     */
    @Deprecated(
        replaceWith = ReplaceWith(
            "fetchMessages(channels = channels, page = PNBoundedPage(start = start, end = end, limit = maximumPerChannel),includeMeta = includeMeta, includeMessageActions = includeMessageActions, includeMessageType = includeMessageType)",
            "com.pubnub.api.models.consumer.PNBoundedPage"
        ),
        level = DeprecationLevel.WARNING,
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
    ) = fetchMessages(
        channels = channels,
        page = PNBoundedPage(start = start, end = end, limit = maximumPerChannel),
        includeMeta = includeMeta,
        includeMessageActions = includeMessageActions,
        includeMessageType = includeMessageType
    )

    /**
     * Fetch historical messages from multiple channels.
     * The `includeMessageActions` flag also allows you to fetch message actions along with the messages.
     *
     * It's possible to control how messages are returned and in what order. For example, you can:
     * - Search for messages starting on the newest end of the timeline.
     * - Search for messages from the oldest end of the timeline.
     * - Page through results by providing a `start` OR `end` time token.
     * - Retrieve a slice of the time line by providing both a `start` AND `end` time token.
     * - Limit the number of messages to a specific quantity using the `limit` parameter.
     * - Batch history returns up to 25 messages per channel, on a maximum of 500 channels.
     * Use the start and end timestamps to page through the next batch of messages.
     *
     * **Start & End parameter usage clarity:**
     * - If you specify only the `start` parameter (without `end`),
     * you will receive messages that are older than and up to that `start` timetoken.
     * - If you specify only the `end` parameter (without `start`),
     * you will receive messages from that `end` timetoken and newer.
     * - Specify values for both `start` and `end` parameters to retrieve messages between those timetokens
     * (inclusive of the `end` value).
     * - Keep in mind that you will still receive a maximum of 25 messages
     * even if there are more messages that meet the timetoken values.
     * - Iterative calls to history adjusting the start timetoken is necessary to page through the full set of results
     * if more than 25 messages meet the timetoken values.
     *
     * @param channels Channels to return history messages from.
     * @param page The paging object used for pagination. @see [PNBoundedPage]
     * @param includeUUID Whether to include publisher uuid with each history message. Defaults to `true`.
     * @param includeMeta Whether to include message metadata in response.
     *                    Defaults to `false`.
     * @param includeMessageActions Whether to include message actions in response.
     *                              Defaults to `false`.
     * @param includeMessageType Whether to include message type in response.
     *                              Defaults to `false`.
     */
    fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage = PNBoundedPage(),
        includeUUID: Boolean = true,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false,
        includeMessageType: Boolean = true
    ) = FetchMessages(
        pubnub = this,
        channels = channels,
        page = page,
        includeUUID = includeUUID,
        includeMeta = includeMeta,
        includeMessageActions = includeMessageActions,
        includeMessageType = includeMessageType
    )

    /**
     * Removes messages from the history of a specific channel.
     *
     * NOTE: There is a setting to accept delete from history requests for a key,
     * which you must enable by checking the Enable `Delete-From-History` checkbox
     * in the key settings for your key in the Administration Portal.
     *
     * Requires Initialization with secret key.
     *
     * @param channels Channels to delete history messages from.
     * @param start Timetoken delimiting the start of time slice (exclusive) to delete messages from.
     * @param end Time token delimiting the end of time slice (inclusive) to delete messages from.
     */
    fun deleteMessages(
        channels: List<String>,
        start: Long? = null,
        end: Long? = null
    ) = DeleteMessages(pubnub = this, channels = channels, start = start, end = end)

    /**
     * Fetches the number of messages published on one or more channels since a given time.
     * The count returned is the number of messages in history with a timetoken value greater
     * than the passed value in the [MessageCounts.channelsTimetoken] parameter.
     *
     * @param channels Channels to fetch the message count from.
     * @param channelsTimetoken List of timetokens, in order of the channels list.
     *                          Specify a single timetoken to apply it to all channels.
     *                          Otherwise, the list of timetokens must be the same length as the list of channels.
     */
    fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>) =
        MessageCounts(pubnub = this, channels = channels, channelsTimetoken = channelsTimetoken)
    //endregion

    //region Presence
    /**
     * Obtain information about the current state of a channel including a list of unique user IDs
     * currently subscribed to the channel and the total occupancy count of the channel.
     *
     * @param channels The channels to get the 'here now' details of.
     *                 Leave empty for a 'global her now'.
     * @param channelGroups The channel groups to get the 'here now' details of.
     *                      Leave empty for a 'global her now'.
     * @param includeState Whether the response should include presence state information, if available.
     *                     Defaults to `false`.
     * @param includeUUIDs Whether the response should include UUIDs od connected clients.
     *                     Defaults to `true`.
     */
    fun hereNow(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        includeState: Boolean = false,
        includeUUIDs: Boolean = true
    ) = HereNow(
        pubnub = this,
        channels = channels,
        channelGroups = channelGroups,
        includeState = includeState,
        includeUUIDs = includeUUIDs
    )

    /**
     * Obtain information about the current list of channels to which a UUID is subscribed to.
     *
     * @param uuid UUID of the user to get its current channel subscriptions. Defaults to the UUID of the client.
     * @see [PNConfiguration.uuid]
     */
    fun whereNow(uuid: String = configuration.userId.value) = WhereNow(pubnub = this, uuid = uuid)

    /**
     * Set state information specific to a subscriber UUID.
     *
     * State information is supplied as a JSON object of key/value pairs.
     *
     * If [PNConfiguration.maintainPresenceState] is `true`, and the `uuid` matches [PNConfiguration.uuid], the state
     * for channels will be saved in the PubNub client and resent with every heartbeat and initial subscribe request.
     * In that case, it's not recommended to mix setting state through channels *and* channel groups, as state set
     * through the channel group will be overwritten after the next heartbeat or subscribe reconnection (e.g. after loss
     * of network).
     *
     * @param channels Channels to set the state to.
     * @param channelGroups Channel groups to set the state to.
     * @param state The actual state object to set.
     *              NOTE: Presence state must be expressed as a JsonObject.
     *              When calling [PubNub.setPresenceState], be sure to supply an initialized JsonObject
     *              or POJO which can be serialized to a JsonObject.
     * @param uuid UUID of the user to set the state for. Defaults to the UUID of the client.
     *             @see [PNConfiguration.uuid]
     */
    fun setPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        state: Any,
        uuid: String = configuration.userId.value
    ): SetState = SetState(
        pubnub = this,
        channels = channels,
        channelGroups = channelGroups,
        state = state,
        uuid = uuid,
        presenceData = presenceData,
    )

    /**
     * Retrieve state information specific to a subscriber UUID.
     *
     * State information is supplied as a JSON object of key/value pairs.
     *
     * @param channels Channels to get the state from.
     * @param channelGroups Channel groups to get the state from.
     * @param uuid UUID of the user to get the state from. Defaults to the UUID of the client.
     *             @see [PNConfiguration.uuid]
     */
    fun getPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        uuid: String = configuration.userId.value
    ) = GetState(pubnub = this, channels = channels, channelGroups = channelGroups, uuid = uuid)

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
    ) = presence.presence(
        channels = channels.toSet(),
        channelGroups = channelGroups.toSet(),
        connected = connected
    )

    //endregion

    //region MessageActions
    /**
     * Add an action on a published message. Returns the added action in the response.
     *
     * @param channel Channel to publish message actions to.
     * @param messageAction The message action object containing the message action's type,
     *                      value and the publish timetoken of the original message.
     */
    fun addMessageAction(channel: String, messageAction: PNMessageAction) =
        AddMessageAction(pubnub = this, channel = channel, messageAction = messageAction)

    /**
     * Remove a previously added action on a published message. Returns an empty response.
     *
     * @param channel Channel to remove message actions from.
     * @param messageTimetoken The publish timetoken of the original message.
     * @param actionTimetoken The publish timetoken of the message action to be removed.
     */
    fun removeMessageAction(channel: String, messageTimetoken: Long, actionTimetoken: Long) =
        RemoveMessageAction(
            pubnub = this,
            channel = channel,
            messageTimetoken = messageTimetoken,
            actionTimetoken = actionTimetoken
        )

    /**
     * Get a list of message actions in a channel. Returns a list of actions in the response.
     *
     * @param channel Channel to fetch message actions from.
     * @param start Message Action timetoken denoting the start of the range requested
     *              (return values will be less than start).
     * @param end Message Action timetoken denoting the end of the range requested
     *            (return values will be greater than or equal to end).
     * @param limit Specifies the number of message actions to return in response.
     */
    @Deprecated(
        replaceWith = ReplaceWith(
            "getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))",
            "com.pubnub.api.models.consumer.PNBoundedPage"
        ),
        level = DeprecationLevel.WARNING,
        message = "Use getMessageActions(String, PNBoundedPage) instead"
    )
    fun getMessageActions(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        limit: Int? = null
    ) = getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))

    /**
     * Get a list of message actions in a channel. Returns a list of actions in the response.
     *
     * @param channel Channel to fetch message actions from.
     * @param page The paging object used for pagination. @see [PNBoundedPage]
     */
    fun getMessageActions(
        channel: String,
        page: PNBoundedPage = PNBoundedPage()
    ) = GetMessageActions(pubnub = this, channel = channel, page = page)
    //endregion

    //region ChannelGroups
    /**
     * Adds a channel to a channel group.
     *
     * @param channels The channels to add to the channel group.
     * @param channelGroup The channel group to add the channels to.
     */
    fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String) =
        AddChannelChannelGroup(pubnub = this, channels = channels, channelGroup = channelGroup)

    /**
     * Lists all the channels of the channel group.
     *
     * @param channelGroup Channel group to fetch the belonging channels.
     */
    fun listChannelsForChannelGroup(channelGroup: String) =
        AllChannelsChannelGroup(pubnub = this, channelGroup = channelGroup)

    /**
     * Removes channels from a channel group.
     *
     * @param channelGroup The channel group to remove channels from
     * @param channels The channels to remove from the channel group.
     */
    fun removeChannelsFromChannelGroup(channels: List<String>, channelGroup: String) =
        RemoveChannelChannelGroup(pubnub = this, channels = channels, channelGroup = channelGroup)

    /**
     * Lists all registered channel groups for the subscribe key.
     */
    fun listAllChannelGroups() = ListAllChannelGroup(this)

    /**
     * Removes the channel group.
     *
     * @param channelGroup The channel group to remove.
     */
    fun deleteChannelGroup(channelGroup: String) =
        DeleteChannelGroup(pubnub = this, channelGroup = channelGroup)
    //endregion

    //region PAM
    /**
     * This function establishes access permissions for PubNub Access Manager (PAM) by setting the `read` or `write`
     * attribute to `true`.
     * A grant with `read` or `write` set to `false` (or not included) will revoke any previous grants
     * with `read` or `write` set to `true`.
     *
     * Permissions can be applied to any one of three levels:
     * - Application level privileges are based on `subscribeKey` applying to all associated channels.
     * - Channel level privileges are based on a combination of `subscribeKey` and `channel` name.
     * - User level privileges are based on the combination of `subscribeKey`, `channel`, and `auth_key`.
     *
     * @param read Set to `true` to request the *read* permission. Defaults to `false`.
     * @param write Set to `true` to request the *write* permission. Defaults to `false`.
     * @param manage Set to `true` to request the *read* permission. Defaults to `false`.
     * @param delete Set to `true` to request the *delete* permission. Defaults to `false`.
     * @param ttl Time in minutes for which granted permissions are valid.
     *            Setting ttl to `0` will apply the grant indefinitely, which is also the default behavior.
     *
     * @param authKeys Specifies authKey to grant permissions to. It's possible to specify multiple auth keys.
     *                 You can also grant access to a single authKey for multiple channels at the same time.
     * @param channels Specifies the channels on which to grant permissions.
     *                 If no channels/channelGroups are specified, then the grant applies to all channels/channelGroups
     *                 that have been or will be created for that publish/subscribe key set.
     *
     *                 Furthermore, any existing or future grants on specific channels are ignored,
     *                 until the all channels grant is revoked.
     *
     *                 It's possible to grant permissions to multiple channels simultaneously.
     *                 Wildcard notation like a.* can be used to grant access on channels. You can grant one level deep.
     *                 - `a.*` - you can grant on this.
     *                 - `a.b.*` - grant won't work on this. If you grant on `a.b.*`,
     *                   the grant will treat `a.b.*` as a single channel with name `a.b.*`.
     * @param channelGroups Specifies the channel groups to grant permissions to.
     *                      If no [channels] or [channelGroups] are specified, then the grant applies to all channels/channelGroups
     *                      that have been or will be created for that publish/subscribe key set.
     *
     *                      Furthermore, any existing or future grants on specific [channelGroups] are ignored,
     *                      until the all [channelGroups] grant is revoked.
     *
     *                      It's possible to grant permissions to multiple [channelGroups] simultaneously.
     */
    fun grant(
        read: Boolean = false,
        write: Boolean = false,
        manage: Boolean = false,
        delete: Boolean = false,
        ttl: Int = -1,
        authKeys: List<String> = emptyList(),
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        uuids: List<String> = emptyList(),
    ) = Grant(
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

    /**
     * See [grant]
     */
    fun grant(
        read: Boolean = false,
        write: Boolean = false,
        manage: Boolean = false,
        delete: Boolean = false,
        get: Boolean = false,
        update: Boolean = false,
        join: Boolean = false,
        ttl: Int = -1,
        authKeys: List<String> = emptyList(),
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        uuids: List<String> = emptyList(),
    ) = Grant(
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

    /**
     * This function generates a grant token for PubNub Access Manager (PAM).
     *
     * Permissions can be applied to any of the three type of resources:
     * - channels
     * - channel groups
     * - uuid - metadata associated with particular UUID
     *
     * Each type of resource have different set of permissions. To know what's possible for each of them
     * check ChannelGrant, ChannelGroupGrant and UUIDGrant.
     *
     * @param ttl Time in minutes for which granted permissions are valid.
     * @param meta Additional metadata
     * @param authorizedUUID Single uuid which is authorized to use the token to make API requests to PubNub
     * @param channels List of all channel grants
     * @param channelGroups List of all channel group grants
     * @param uuids List of all uuid grants
     */

    fun grantToken(
        ttl: Int,
        meta: Any? = null,
        authorizedUUID: String? = null,
        channels: List<ChannelGrant> = emptyList(),
        channelGroups: List<ChannelGroupGrant> = emptyList(),
        uuids: List<UUIDGrant> = emptyList()
    ): GrantToken {
        return GrantToken(
            pubnub = this,
            ttl = ttl,
            meta = meta,
            authorizedUUID = authorizedUUID,
            channels = channels,
            channelGroups = channelGroups,
            uuids = uuids
        )
    }

    /**
     * This function generates a grant token for PubNub Access Manager (PAM).
     *
     * Permissions can be applied to any of the two type of resources:
     * - spacePermissions
     * - userPermissions
     *
     * Each type of resource have different set of permissions. To know what's possible for each of them
     * check SpacePermissions and UserPermissions.
     *
     * @param ttl Time in minutes for which granted permissions are valid.
     * @param meta Additional metadata
     * @param authorizedUserId Single userId which is authorized to use the token to make API requests to PubNub
     * @param spacesPermissions List of all space grants
     * @param usersPermissions List of all userId grants
     */
    fun grantToken(
        ttl: Int,
        meta: Any? = null,
        authorizedUserId: UserId? = null,
        spacesPermissions: List<SpacePermissions> = emptyList(),
        usersPermissions: List<UserPermissions> = emptyList()
    ): GrantToken {
        return GrantToken(
            pubnub = this,
            ttl = ttl,
            meta = meta,
            authorizedUUID = authorizedUserId?.value,
            channels = spacesPermissions.map { spacePermissions -> spacePermissions.toChannelGrant() },
            channelGroups = emptyList(),
            uuids = usersPermissions.map { userPermissions -> userPermissions.toUuidGrant() }
        )
    }

    /**
     * This method allows you to disable an existing token and revoke all permissions embedded within.
     *
     * @param token Existing token with embedded permissions.
     */
    fun revokeToken(token: String): RevokeToken {
        return RevokeToken(
            pubnub = this,
            token = token
        )
    }
    //endregion

    //region Miscellaneous
    /**
     * Returns a 17 digit precision Unix epoch from the server.
     */
    fun time() = Time(this)
    //endregion

    //region ObjectsAPI
    /**
     * Returns a paginated list of Channel Metadata objects, optionally including the custom data object for each.
     *
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     */
    fun getAllChannelMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ) = GetAllChannelMetadata(
        pubnub = this,
        collectionQueryParameters = CollectionQueryParameters(
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount
        ),
        includeQueryParam = IncludeQueryParam(includeCustom = includeCustom)
    )

    /**
     * Returns metadata for the specified Channel, optionally including the custom data object for each.
     *
     * @param channel Channel name.
     * @param includeCustom Include respective additional fields in the response.
     */
    fun getChannelMetadata(channel: String, includeCustom: Boolean = false) = GetChannelMetadata(
        pubnub = this,
        channel = channel,
        includeQueryParam = IncludeQueryParam(includeCustom = includeCustom)
    )

    /**
     * Set metadata for a Channel in the database, optionally including the custom data object for each.
     *
     * @param channel Channel name.
     * @param name Name of a channel.
     * @param description Description of a channel.
     * @param custom Object with supported data types.
     * @param includeCustom Include respective additional fields in the response.
     */
    fun setChannelMetadata(
        channel: String,
        name: String? = null,
        description: String? = null,
        custom: Any? = null,
        includeCustom: Boolean = false,
        type: String? = null,
        status: String? = null
    ) = SetChannelMetadata(
        pubnub = this,
        channel = channel,
        name = name,
        description = description,
        custom = custom,
        includeQueryParam = IncludeQueryParam(includeCustom = includeCustom),
        type = type,
        status = status
    )

    /**
     * Removes the metadata from a specified channel.
     *
     * @param channel Channel name.
     */
    fun removeChannelMetadata(channel: String) = RemoveChannelMetadata(this, channel = channel)

    /**
     * Returns a paginated list of UUID Metadata objects, optionally including the custom data object for each.
     *
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     */
    fun getAllUUIDMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ) = GetAllUUIDMetadata(
        pubnub = this,
        collectionQueryParameters = CollectionQueryParameters(
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount
        ),
        withInclude = IncludeQueryParam(includeCustom = includeCustom)
    )

    /**
     * Returns metadata for the specified UUID, optionally including the custom data object for each.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param includeCustom Include respective additional fields in the response.
     */
    fun getUUIDMetadata(
        uuid: String? = null,
        includeCustom: Boolean = false
    ) = GetUUIDMetadata(
        pubnub = this,
        uuid = uuid ?: configuration.userId.value,
        includeQueryParam = IncludeQueryParam(includeCustom = includeCustom)
    )

    /**
     * Set metadata for a UUID in the database, optionally including the custom data object for each.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param name Display name for the user. Maximum 200 characters.
     * @param externalId User's identifier in an external system
     * @param profileUrl The URL of the user's profile picture
     * @param email The user's email address. Maximum 80 characters.
     * @param custom Object with supported data types.
     * @param includeCustom Include respective additional fields in the response.
     */
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
    ) = SetUUIDMetadata(
        pubnub = this,
        uuid = uuid,
        name = name,
        externalId = externalId,
        profileUrl = profileUrl,
        email = email,
        custom = custom,
        withInclude = IncludeQueryParam(includeCustom = includeCustom),
        type = type,
        status = status
    )

    /**
     * Removes the metadata from a specified UUID.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     */
    fun removeUUIDMetadata(uuid: String? = null) = RemoveUUIDMetadata(pubnub = this, uuid = uuid)

    /**
     * The method returns a list of channel memberships for a user. This method doesn't return a user's subscriptions.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeChannelDetails Include custom fields for channels metadata.
     */
    fun getMemberships(
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ) = GetMemberships(
        pubnub = this,
        uuid = uuid ?: configuration.userId.value,
        collectionQueryParameters = CollectionQueryParameters(
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount
        ),
        includeQueryParam = IncludeQueryParam(
            includeCustom = includeCustom,
            includeChannelDetails = includeChannelDetails,
            includeType = false
        )
    )

    /**
     * @see [PubNub.setMemberships]
     */
    @Deprecated(
        replaceWith = ReplaceWith(
            "setMemberships(channels = channels, uuid = uuid, limit = limit, " +
                "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                "includeChannelDetails = includeChannelDetails)"
        ),
        level = DeprecationLevel.WARNING,
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
    ) = setMemberships(
        channels = channels,
        uuid = uuid ?: configuration.userId.value,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeChannelDetails = includeChannelDetails
    )

    /**
     * Set channel memberships for a UUID.
     *
     * @param channels List of channels to add to membership. List can contain strings (channel-name only)
     *                 or objects (which can include custom data). @see [PNChannelWithCustom]
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeChannelDetails Include custom fields for channels metadata.
     */
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
    ) = manageMemberships(
        channelsToSet = channels,
        channelsToRemove = listOf(),
        uuid = uuid ?: configuration.userId.value,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeChannelDetails = includeChannelDetails,
    )

    /**
     * Remove channel memberships for a UUID.
     *
     * @param channels Channels to remove from membership.
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeChannelDetails Include custom fields for channels metadata.
     */
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
    ) = manageMemberships(
        channelsToSet = listOf(),
        channelsToRemove = channels,
        uuid = uuid ?: configuration.userId.value,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeChannelDetails = includeChannelDetails
    )

    /**
     * Add and remove channel memberships for a UUID.
     *
     * @param channelsToSet Collection of channels to add to membership. @see [com.pubnub.api.models.consumer.objects.membership.PNChannelMembership.Partial]
     * @param channelsToRemove Channels to remove from membership.
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeChannelDetails Include custom fields for channels metadata.
     */
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
    ) = ManageMemberships(
        pubnub = this,
        channelsToSet = channelsToSet,
        channelsToRemove = channelsToRemove,
        uuid = uuid ?: configuration.userId.value,
        collectionQueryParameters = CollectionQueryParameters(
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount
        ),
        includeQueryParam = IncludeQueryParam(
            includeCustom = includeCustom,
            includeChannelDetails = includeChannelDetails,
            includeType = false
        )
    )

    /**
     * @see [PubNub.getChannelMembers]
     */
    @Deprecated(
        replaceWith = ReplaceWith(
            "getChannelMembers(channel = channel, limit = limit, " +
                "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                "includeUUIDDetails = includeUUIDDetails)"
        ),
        level = DeprecationLevel.WARNING,
        message = "Use getChannelMembers instead"
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
    ) = getChannelMembers(
        channel = channel,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeUUIDDetails = includeUUIDDetails
    )

    /**
     * The method returns a list of members in a channel. The list will include user metadata for members
     * that have additional metadata stored in the database.
     *
     * @param channel Channel name
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeUUIDDetails Include custom fields for UUIDs metadata.
     */
    fun getChannelMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ) = GetChannelMembers(
        pubnub = this,
        channel = channel,
        collectionQueryParameters = CollectionQueryParameters(
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount
        ),
        includeQueryParam = IncludeQueryParam(
            includeCustom = includeCustom,
            includeUUIDDetails = includeUUIDDetails,
            includeType = false
        )
    )

    /**
     * @see [PubNub.setChannelMembers]
     */
    @Deprecated(
        replaceWith = ReplaceWith(
            "setChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                "includeUUIDDetails = includeUUIDDetails)"
        ),
        level = DeprecationLevel.WARNING,
        message = "Use setChannelMembers instead"
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
    ) = setChannelMembers(
        channel = channel,
        uuids = uuids,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeUUIDDetails = includeUUIDDetails
    )

    /**
     * This method sets members in a channel.
     *
     * @param channel Channel name
     * @param uuids List of members to add to the channel. List can contain strings (uuid only)
     *              or objects (which can include custom data). @see [PNMember.Partial]
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeUUIDDetails Include custom fields for UUIDs metadata.
     */
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
    ) = manageChannelMembers(
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
    )

    /**
     * @see [PubNub.removeChannelMembers]
     */
    @Deprecated(
        replaceWith = ReplaceWith(
            "removeChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                "includeUUIDDetails = includeUUIDDetails)"
        ),
        level = DeprecationLevel.WARNING,
        message = "Use removeChannelMembers instead"
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
    ) = removeChannelMembers(
        channel = channel,
        uuids = uuids,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeUUIDDetails = includeUUIDDetails
    )

    /**
     * Remove members from a Channel.
     *
     * @param channel Channel name
     * @param uuids Members to remove from channel.
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeUUIDDetails Include custom fields for UUIDs metadata.
     */
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
    ) = manageChannelMembers(
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
    )

    /**
     * Set or remove members in a channel.
     *
     * @param channel Channel name
     * @param uuidsToSet Collection of members to add to the channel. @see [com.pubnub.api.models.consumer.objects.member.PNMember.Partial]
     * @param uuidsToRemove Members to remove from channel.
     * @param limit Number of objects to return in the response.
     *              Default is 100, which is also the maximum value.
     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
     * @param page Use for pagination.
     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
     *                           Ignored if you also supply the start parameter.
     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
     *               expression are returned.
     * @param sort List of properties to sort by. Available options are id, name, and updated.
     *             @see [PNAsc], [PNDesc]
     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
     *                     Default is `false`.
     * @param includeCustom Include respective additional fields in the response.
     * @param includeUUIDDetails Include custom fields for UUIDs metadata.
     */
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
    ) = ManageChannelMembers(
        pubnub = this,
        channel = channel,
        uuidsToSet = uuidsToSet,
        uuidsToRemove = uuidsToRemove,
        collectionQueryParameters = CollectionQueryParameters(
            limit = limit,
            page = page,
            filter = filter,
            sort = sort,
            includeCount = includeCount
        ),
        includeQueryParam = IncludeQueryParam(
            includeCustom = includeCustom,
            includeUUIDDetails = includeUUIDDetails,
            includeType = false
        )
    )

    //endregion ObjectsAPI

    //region files
    /**
     * Upload file / data to specified Channel.
     *
     * @param channel Channel name
     * @param fileName Name of the file to send.
     * @param inputStream Input stream with file content. The inputStream will be depleted after the call.
     * @param message The payload.
     *                **Warning:** It is important to note that you should not serialize JSON
     *                when sending signals/messages via PubNub.
     *                Why? Because the serialization is done for you automatically.
     *                Instead just pass the full object as the message payload.
     *                PubNub takes care of everything for you.
     * @param meta Metadata object which can be used with the filtering ability.
     * @param ttl Set a per message time to live in storage.
     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
     *              with no expiry time.
     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
     *              the message is stored with an expiry time of `X` hours.
     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
     *            - If ttl isn't specified, then expiration of the message defaults
     *              back to the expiry value for the key.
     * @param shouldStore Store in history.
     *                    If not specified, then the history configuration of the key is used.
     * @param cipherKey Key to be used to encrypt uploaded data. If not provided,
     *                  cipherKey in @see [PNConfiguration] will be used, if provided.
     */
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
        val cryptoModule = if (cipherKey != null) {
            CryptoModule.createLegacyCryptoModule(cipherKey)
        } else {
            cryptoModule
        }

        return SendFile(
            channel = channel,
            fileName = fileName,
            inputStream = inputStream,
            message = message,
            meta = meta,
            ttl = ttl,
            shouldStore = shouldStore,
            executorService = retrofitManager.getTransactionClientExecutorService(),
            fileMessagePublishRetryLimit = configuration.fileMessagePublishRetryLimit,
            generateUploadUrlFactory = GenerateUploadUrl.Factory(this),
            publishFileMessageFactory = PublishFileMessage.Factory(this),
            sendFileToS3Factory = UploadFile.Factory(this),
            cryptoModule = cryptoModule
        )
    }

    /**
     * Retrieve list of files uploaded to Channel.
     *
     * @param channel Channel name
     * @param limit Number of files to return. Minimum value is 1, and maximum is 100. Default value is 100.
     * @param next Previously-returned cursor bookmark for fetching the next page. @see [PNPage.PNNext]
     */
    fun listFiles(
        channel: String,
        limit: Int? = null,
        next: PNPage.PNNext? = null
    ): ListFiles {
        return ListFiles(
            pubNub = this,
            channel = channel,
            limit = limit,
            next = next
        )
    }

    /**
     * Generate URL which can be used to download file from target Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     */
    fun getFileUrl(
        channel: String,
        fileName: String,
        fileId: String
    ): GetFileUrl {
        return GetFileUrl(
            pubNub = this,
            channel = channel,
            fileName = fileName,
            fileId = fileId
        )
    }

    /**
     * Download file from specified Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     * @param cipherKey Key to be used to decrypt downloaded data. If a key is not provided,
     *                  the SDK uses the cipherKey from the @see [PNConfiguration].
     */
    fun downloadFile(
        channel: String,
        fileName: String,
        fileId: String,
        cipherKey: String? = null
    ): DownloadFile {
        val cryptoModule = if (cipherKey != null) {
            CryptoModule.createLegacyCryptoModule(cipherKey)
        } else {
            cryptoModule
        }

        return DownloadFile(
            pubNub = this,
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            cryptoModule = cryptoModule
        )
    }

    /**
     * Delete file from specified Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     */
    fun deleteFile(
        channel: String,
        fileName: String,
        fileId: String
    ): DeleteFile {
        return DeleteFile(
            pubNub = this,
            channel = channel,
            fileName = fileName,
            fileId = fileId
        )
    }

    /**
     * Publish file message from specified Channel.
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     * @param message The payload.
     *                **Warning:** It is important to note that you should not serialize JSON
     *                when sending signals/messages via PubNub.
     *                Why? Because the serialization is done for you automatically.
     *                Instead just pass the full object as the message payload.
     *                PubNub takes care of everything for you.
     * @param meta Metadata object which can be used with the filtering ability.
     * @param ttl Set a per message time to live in storage.
     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
     *              with no expiry time.
     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
     *              the message is stored with an expiry time of `X` hours.
     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
     *            - If ttl isn't specified, then expiration of the message defaults
     *              back to the expiry value for the key.
     * @param shouldStore Store in history.
     *                    If not specified, then the history configuration of the key is used.
     *
     */
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
            pubNub = this,
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            message = message,
            meta = meta,
            ttl = ttl,
            shouldStore = shouldStore
        )
    }
    //endregion

    //region Encryption
    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
     *
     * @param inputString String to be decrypted.
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    fun decrypt(inputString: String): String = decrypt(inputString, null)

    /**
     * Perform Cryptographic decryption of an input string using a cipher key.
     *
     * @param inputString String to be decrypted.
     * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    fun decrypt(inputString: String, cipherKey: String? = null): String =
        getCryptoModuleOrThrow(cipherKey).decryptString(inputString)

    /**
     * Perform Cryptographic decryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for decryption. If not provided [PNConfiguration.cipherKey] is used.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed decryption.
     */
    fun decryptInputStream(
        inputStream: InputStream,
        cipherKey: String? = null
    ): InputStream = getCryptoModuleOrThrow(cipherKey).decryptStream(inputStream)

    /**
     * Perform Cryptographic encryption of an input string and a cipher key.
     *
     * @param inputString String to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the encryption of `inputString` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    fun encrypt(inputString: String, cipherKey: String? = null): String =
        getCryptoModuleOrThrow(cipherKey).encryptString(inputString)

    /**
     * Perform Cryptographic encryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. If not provided [PNConfiguration.cipherKey] is used.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    fun encryptInputStream(
        inputStream: InputStream,
        cipherKey: String? = null
    ): InputStream = getCryptoModuleOrThrow(cipherKey).encryptStream(inputStream)

    private fun getCryptoModuleOrThrow(cipherKey: String? = null): CryptoModule {
        return cipherKey?.let {
            CryptoModule.createLegacyCryptoModule(it, configuration.useRandomInitializationVector)
        } ?: cryptoModule ?: throw PubNubException("Crypto module is not initialized")
    }
    //endregion

    /**
     * Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status]
     *
     * @param timetoken optional timetoken to use for the subscription on reconnection.
     * Only applicable when [PNConfiguration.enableEventEngine] is true, otherwise ignored
     */
    fun reconnect(timetoken: Long = 0L) {
        subscribe.reconnect(timetoken)
        presence.reconnect()
    }

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    fun disconnect() {
        subscribe.disconnect()
        presence.disconnect()
    }

    /**
     * Frees up threads and allows for a clean exit.
     */
    fun destroy() {
        subscribe.destroy()
        presence.destroy()

        retrofitManager.destroy()
        executorService.shutdown()
    }

    /**
     * Same as [destroy] but immediately.
     */
    fun forceDestroy() {
        subscribe.destroy()
        presence.destroy()

        retrofitManager.destroy(true)
        executorService.shutdownNow()
    }

    fun parseToken(token: String): PNToken {
        return tokenParser.unwrapToken(token)
    }

    fun setToken(token: String?) {
        return tokenManager.setToken(token)
    }

    // begin V2
    // public

    /**
     * Create a handle to a [Channel] that can be used to obtain a [Subscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel is required.
     *
     * The returned [Channel] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel to return. Supports wildcards by ending it with ".*". See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/overview)
     *
     * @return a [Channel] instance representing the channel with the given [name]
     */
    fun channel(name: String): Channel {
        return ChannelImpl(basePubNub, ChannelName(name))
    }

    /**
     * Create a handle to a [ChannelGroup] that can be used to obtain a [Subscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel group is required.
     *
     * The returned [ChannelGroup] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel group to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups)
     *
     * @return a [ChannelGroup] instance representing the channel group with the given [name]
     */
    fun channelGroup(name: String): ChannelGroup {
        return ChannelGroupImpl(basePubNub, ChannelGroupName(name))
    }

    /**
     * Create a handle to a [ChannelMetadata] object that can be used to obtain a [Subscription] to metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a metadata channel is required.
     *
     * The returned [ChannelMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the channel metadata to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata)
     *
     * @return a [ChannelMetadata] instance representing the channel metadata with the given [id]
     */
    fun channelMetadata(id: String): ChannelMetadata {
        return ChannelMetadataImpl(basePubNub, ChannelName(id))
    }

    /**
     * Create a handle to a [UserMetadata] object that can be used to obtain a [Subscription] to user metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a user metadata is required.
     *
     * The returned [UserMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the user. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata)
     *
     * @return a [UserMetadata] instance representing the channel metadata with the given [id]
     */
    fun userMetadata(id: String): UserMetadata {
        return UserMetadataImpl(basePubNub, ChannelName(id))
    }

    /**
     * Create a [SubscriptionSet] from the given [subscriptions].
     *
     * @param subscriptions the subscriptions that will be added to the returned [SubscriptionSet]
     * @return a [SubscriptionSet] containing all [subscriptions]
     */
    fun subscriptionSetOf(subscriptions: Set<Subscription> = emptySet()): SubscriptionSet {
        @Suppress("UNCHECKED_CAST")
        return SubscriptionSetImpl(basePubNub, subscriptions as Set<SubscriptionImpl>)
    }

    /**
     * Create a [SubscriptionSet] containing [Subscription] objects for the given sets of [channels] and
     * [channelGroups].
     *
     * Please note that the subscriptions are not active until you call [SubscriptionSet.subscribe].
     *
     * This is a convenience method, and it is equal to calling [PubNub.channel] followed by [Channel.subscription] for
     * each channel, then creating a [subscriptionSetOf] using the returned [Subscription] objects (and similarly for
     * channel groups).
     *
     * @param channels the channels to create subscriptions for
     * @param channelGroups the channel groups to create subscriptions for
     * @param options the [SubscriptionOptions] to pass for each subscription. Refer to supported options in [Channel] and
     * [ChannelGroup] documentation.
     * @return a [SubscriptionSet] containing subscriptions for the given [channels] and [channelGroups]
     */
    fun subscriptionSetOf(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
        options: SubscriptionOptions = EmptyOptions
    ): SubscriptionSet {
        val subscriptionSet = SubscriptionSetImpl(basePubNub)
        channels.forEach {
            subscriptionSet.add(channel(it).subscription(options))
        }
        channelGroups.forEach {
            subscriptionSet.add(channelGroup(it).subscription(options))
        }
        return subscriptionSet
    }

    // internal
    private val LOCK_CHANNELS_AND_GROUPS = Any()
    private val channelSubscriptions = mutableMapOf<ChannelName, MutableSet<Subscription>>()
    private val channelGroupSubscriptions = mutableMapOf<ChannelGroupName, MutableSet<Subscription>>()

    internal fun subscribe(vararg subscriptions: SubscriptionImpl, cursor: SubscriptionCursor) {
        synchronized(LOCK_CHANNELS_AND_GROUPS) {
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

            val (channelsWithPresence, channelsNoPresence) = channelsToSubscribe.filter { !it.isPresence }
                .partition {
                    channelsToSubscribe.contains(it.withPresence)
                }
            val (groupsWithPresence, groupsNoPresence) = groupsToSubscribe.filter { !it.isPresence }.partition {
                groupsToSubscribe.contains(it.withPresence)
            }
            if (channelsWithPresence.isNotEmpty() || groupsWithPresence.isNotEmpty()) {
                subscribeInternal(
                    channels = channelsWithPresence.map(ChannelName::id),
                    channelGroups = groupsWithPresence.map(ChannelGroupName::id),
                    withPresence = true,
                    withTimetoken = cursor.timetoken
                )
            }
            if (channelsNoPresence.isNotEmpty() || groupsNoPresence.isNotEmpty()) {
                subscribeInternal(
                    channels = channelsNoPresence.map(ChannelName::id),
                    channelGroups = groupsNoPresence.map(ChannelGroupName::id),
                    withPresence = false,
                    withTimetoken = cursor.timetoken
                )
            }
        }
    }

    internal fun unsubscribe(vararg subscriptions: SubscriptionImpl) {
        synchronized(LOCK_CHANNELS_AND_GROUPS) {
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
                    channelGroups = groupsToUnsubscribe.map(ChannelGroupName::id)
                )
            }
        }
    }
}
