package com.pubnub.api

import com.pubnub.api.callbacks.SubscribeCallback
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
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.history.PNHistoryResult
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
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.StatusEmitter
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.kmp.CustomObject
import java.io.InputStream
import java.util.UUID

actual interface PubNub : StatusEmitter, EventEmitter {
    val timestamp: Int
    val baseUrl: String

    /**
     * The current version of the PubNub SDK.
     */
    val version: String

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
    actual fun channel(name: String): Channel

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
    actual fun channelGroup(name: String): ChannelGroup

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
    actual fun channelMetadata(id: String): ChannelMetadata

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
    actual fun userMetadata(id: String): UserMetadata

    /**
     * Create a [SubscriptionSet] from the given [subscriptions].
     *
     * @param subscriptions the subscriptions that will be added to the returned [SubscriptionSet]
     * @return a [SubscriptionSet] containing all [subscriptions]
     */
    actual fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet

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
    actual fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions,
    ): SubscriptionSet

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
     *
     * @param inputString String to be decrypted.
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    @Throws(PubNubException::class)
    fun decrypt(inputString: String): String

    /**
     * Perform Cryptographic decryption of an input string using a cipher key.
     *
     * @param inputString String to be decrypted.
     * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    @Throws(PubNubException::class)
    fun decrypt(
        inputString: String,
        cipherKey: String? = null,
    ): String

    /**
     * Perform Cryptographic decryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for decryption.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed decryption.
     */
    @Throws(PubNubException::class)
    fun decryptInputStream(
        inputStream: InputStream,
        cipherKey: String? = null,
    ): InputStream

    /**
     * Perform Cryptographic encryption of an input string and a cipher key.
     *
     * @param inputString String to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the encryption of `inputString` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    @Throws(PubNubException::class)
    fun encrypt(
        inputString: String,
        cipherKey: String? = null,
    ): String

    /**
     * Perform Cryptographic encryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for encryption.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    @Throws(PubNubException::class)
    fun encryptInputStream(
        inputStream: InputStream,
        cipherKey: String? = null,
    ): InputStream

    @Throws(PubNubException::class)
    actual fun parseToken(token: String): PNToken

    actual fun setToken(token: String?)

    actual fun getToken(): String?

    /**
     * Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status]
     *
     * @param timetoken optional timetoken to use for the subscriptions on reconnection.
     */
    fun reconnect(timetoken: Long = 0L)

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    fun disconnect()

    /**
     * Unsubscribe from all channels and all channel groups.
     */
    actual fun unsubscribeAll()

    /**
     * Frees up threads eventually and allows for a clean exit.
     */
    actual fun destroy()

    /**
     * Same as [destroy] but immediately.
     */
    fun forceDestroy()

    companion object {
        /**
         * Initialize and return an instance of the PubNub client.
         * @param configuration the configuration to use
         * @return the PubNub client
         */
        @JvmStatic
        fun create(configuration: PNConfiguration): PubNub {
            return Class.forName(
                "com.pubnub.internal.PubNubImpl",
            ).getConstructor(PNConfiguration::class.java).newInstance(configuration) as PubNub
        }

        @JvmStatic
        fun create(
            userId: UserId,
            subscribeKey: String,
            builder: PNConfiguration.Builder.() -> Unit,
        ): PubNub {
            return Class.forName(
                "com.pubnub.internal.PubNubImpl",
            ).getConstructor(PNConfiguration::class.java)
                .newInstance(
                    PNConfiguration.builder(userId, subscribeKey, builder).build()
                ) as PubNub
        }

        /**
         * Generates random UUID to use. You should set a unique UUID to identify the user or the device
         * that connects to PubNub.
         */
        @JvmStatic
        fun generateUUID(): String = "pn-${UUID.randomUUID()}"
    }

    /**
     * The configuration that was used to initialize this PubNub instance.
     * Modifying the values in this configuration is not advised, as it may lead
     * to undefined behavior.
     */
    actual val configuration: PNConfiguration

    /**
     * Add a legacy listener for both client status and events.
     * Prefer `addListener(EventListener)` and `addListener(StatusListener)` if possible.
     *
     * @param listener The listener to be added.
     */
    fun addListener(listener: SubscribeCallback)

    //region api

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
     * @param customMessageType The custom type associated with the message.
     */
    actual fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?,
        customMessageType: String?,
    ): Publish

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
     */
    actual fun fire(
        channel: String,
        message: Any,
        meta: Any?,
        usePost: Boolean,
    ): Publish

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
     */
    @Deprecated(
        "`fire()` never used the `ttl` parameter, please use the version without `ttl`.",
        replaceWith = ReplaceWith("fire(channel, message, meta, usePost)")
    )
    fun fire(
        channel: String,
        message: Any,
        meta: Any?,
        usePost: Boolean,
        ttl: Int?,
    ): Publish

    /**
     * Send a signal to all subscribers of a channel.
     *
     * By default, signals are limited to a message payload size of 30 bytes.
     * This limit applies only to the payload, and not to the URI or headers.
     * If you require a larger payload size, please [contact support](mailto:support@pubnub.com).
     *
     * @param channel The channel which the signal will be sent to.
     * @param message The payload which will be serialized and sent.
     * @param customMessageType The custom type associated with the message.
     */
    actual fun signal(
        channel: String,
        message: Any,
        customMessageType: String?,
    ): Signal

    /**
     * Queries the local subscribe loop for channels currently in the mix.
     *
     * @return A list of channels the client is currently subscribed to.
     */
    actual fun getSubscribedChannels(): List<String>

    /**
     * Queries the local subscribe loop for channel groups currently in the mix.
     *
     * @return A list of channel groups the client is currently subscribed to.
     */
    actual fun getSubscribedChannelGroups(): List<String>

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
    actual fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): AddChannelsToPush

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
    actual fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): ListPushProvisions

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
    actual fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): RemoveChannelsFromPush

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
    actual fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): RemoveAllPushChannelsForDevice

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "Use fetchMessages(List<String>, PNBoundedPage, Boolean, Boolean, Boolean, Boolean, Boolean) instead",
    )
    fun history(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        count: Int = PNHistoryResult.MAX_COUNT,
        reverse: Boolean = false,
        includeTimetoken: Boolean = false,
        includeMeta: Boolean = false,
    ): History

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
        replaceWith =
            ReplaceWith(
                "fetchMessages(channels = channels, page = PNBoundedPage(start = start, end = end, " +
                    "limit = maximumPerChannel),includeMeta = includeMeta, " +
                    "includeMessageActions = includeMessageActions, includeMessageType = includeMessageType)",
                "com.pubnub.api.models.consumer.PNBoundedPage",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use fetchMessages(List<String>, PNBoundedPage, Boolean, Boolean, Boolean, Boolean, Boolean) instead",
    )
    fun fetchMessages(
        channels: List<String>,
        maximumPerChannel: Int = 0,
        start: Long? = null,
        end: Long? = null,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false,
        includeMessageType: Boolean = true,
        includeCustomMessageType: Boolean = false
    ): FetchMessages

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
     * @param includeCustomMessageType Whether to include custom message type in response. Default to `false`
     */
    actual fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean,
        includeCustomMessageType: Boolean
    ): FetchMessages

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
    actual fun deleteMessages(
        channels: List<String>,
        start: Long?,
        end: Long?,
    ): DeleteMessages

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
    actual fun messageCounts(
        channels: List<String>,
        channelsTimetoken: List<Long>,
    ): MessageCounts

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
    actual fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean,
    ): HereNow

    /**
     * Obtain information about the current list of channels to which a UUID is subscribed to.
     *
     * @param uuid UUID of the user to get its current channel subscriptions. Defaults to the UUID of the client.
     * @see [PNConfiguration.uuid]
     */
    actual fun whereNow(uuid: String): WhereNow

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
     * @param state The override state object to set.
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
        uuid: String = configuration.userId.value,
    ): SetState

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
     * @param state The override state object to set.
     *              NOTE: Presence state must be expressed as a JsonObject.
     *              When calling [PubNub.setPresenceState], be sure to supply an initialized JsonObject
     *              or POJO which can be serialized to a JsonObject.
     */
    actual fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
    ): SetState

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
    actual fun getPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        uuid: String,
    ): GetState

    /**
     * Track the online and offline status of users and devices in real time and store custom state information.
     * When you have Presence enabled, PubNub automatically creates a presence channel for each channel.
     *
     * Subscribing to a presence channel or presence channel group will only return presence events
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    actual fun presence(
        channels: List<String>,
        channelGroups: List<String>,
        connected: Boolean,
    )

    /**
     * Add an action on a published message. Returns the added action in the response.
     *
     * @param channel Channel to publish message actions to.
     * @param messageAction The message action object containing the message action's type,
     *                      value and the publish timetoken of the original message.
     */
    actual fun addMessageAction(
        channel: String,
        messageAction: PNMessageAction,
    ): AddMessageAction

    /**
     * Remove a previously added action on a published message. Returns an empty response.
     *
     * @param channel Channel to remove message actions from.
     * @param messageTimetoken The publish timetoken of the original message.
     * @param actionTimetoken The publish timetoken of the message action to be removed.
     */
    actual fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long,
    ): RemoveMessageAction

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
        replaceWith =
            ReplaceWith(
                "getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))",
                "com.pubnub.api.models.consumer.PNBoundedPage",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use getMessageActions(String, PNBoundedPage) instead",
    )
    fun getMessageActions(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        limit: Int? = null,
    ): GetMessageActions

    /**
     * Get a list of message actions in a channel. Returns a list of actions in the response.
     *
     * @param channel Channel to fetch message actions from.
     * @param page The paging object used for pagination. @see [PNBoundedPage]
     */
    actual fun getMessageActions(
        channel: String,
        page: PNBoundedPage,
    ): GetMessageActions

    /**
     * Adds a channel to a channel group.
     *
     * @param channels The channels to add to the channel group.
     * @param channelGroup The channel group to add the channels to.
     */
    actual fun addChannelsToChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): AddChannelChannelGroup

    /**
     * Lists all the channels of the channel group.
     *
     * @param channelGroup Channel group to fetch the belonging channels.
     */
    actual fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup

    /**
     * Removes channels from a channel group.
     *
     * @param channelGroup The channel group to remove channels from
     * @param channels The channels to remove from the channel group.
     */
    actual fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): RemoveChannelChannelGroup

    /**
     * Lists all registered channel groups for the subscribe key.
     */
    actual fun listAllChannelGroups(): ListAllChannelGroup

    /**
     * Removes the channel group.
     *
     * @param channelGroup The channel group to remove.
     */
    actual fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup

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
    ): Grant

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
    ): Grant

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

    actual fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>,
    ): GrantToken

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
        usersPermissions: List<UserPermissions> = emptyList(),
    ): GrantToken

    /**
     * This method allows you to disable an existing token and revoke all permissions embedded within.
     *
     * @param token Existing token with embedded permissions.
     */
    actual fun revokeToken(token: String): RevokeToken

    /**
     * Returns a 17 digit precision Unix epoch from the server.
     */
    actual fun time(): Time

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
    actual fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
    ): GetAllChannelMetadata

    /**
     * Returns metadata for the specified Channel, optionally including the custom data object for each.
     *
     * @param channel Channel name.
     * @param includeCustom Include respective additional fields in the response.
     */
    actual fun getChannelMetadata(
        channel: String,
        includeCustom: Boolean,
    ): GetChannelMetadata

    /**
     * Set metadata for a Channel in the database, optionally including the custom data object for each.
     *
     * @param channel Channel name.
     * @param name Name of a channel.
     * @param description Description of a channel.
     * @param custom Object with supported data types.
     * @param includeCustom Include respective additional fields in the response.
     */
    actual fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: CustomObject?,
        includeCustom: Boolean,
        type: String?,
        status: String?,
    ): SetChannelMetadata

    /**
     * Removes the metadata from a specified channel.
     *
     * @param channel Channel name.
     */
    actual fun removeChannelMetadata(channel: String): RemoveChannelMetadata

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
    actual fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
    ): GetAllUUIDMetadata

    /**
     * Returns metadata for the specified UUID, optionally including the custom data object for each.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param includeCustom Include respective additional fields in the response.
     */
    actual fun getUUIDMetadata(
        uuid: String?,
        includeCustom: Boolean,
    ): GetUUIDMetadata

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
    actual fun setUUIDMetadata(
        uuid: String?,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?,
    ): SetUUIDMetadata

    /**
     * Removes the metadata from a specified UUID.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     */
    actual fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new getMemberships(userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "getMemberships(userId, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeType = includeType))"
        )
    )
    actual fun getMemberships(
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
        includeType: Boolean,
    ): GetMemberships

    /**
     * The method returns a list of channel memberships for a user. This method doesn't return a user's subscriptions.
     *
     * @param userId Unique user identifier. If not supplied then current user’s id is used.
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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.membership.MembershipInclude] to easily create the desired configuration.
     */
    actual fun getMemberships(
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude
    ): GetMemberships

    /**
     * @see [PubNub.setMemberships]
     */
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
    fun addMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null,
    ): ManageMemberships

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new setMemberships(channels, userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "setMemberships(channels, userId, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeType = includeType))"
        )
    )
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
        includeType: Boolean,
    ): ManageMemberships

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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.membership.MembershipInclude] to easily create the desired configuration.
     */
    actual fun setMemberships(
        channels: List<ChannelMembershipInput>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude,
    ): ManageMemberships

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new removeMemberships(channels, userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "removeMemberships(channels, userId, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeType = includeType))"
        )
    )
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
        includeType: Boolean,
    ): ManageMemberships

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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.membership.MembershipInclude] to easily create the desired configuration.
     */
    actual fun removeMemberships(
        channels: List<String>,
        userId: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        include: MembershipInclude
    ): ManageMemberships

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new manageMemberships(channels, userId, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "manageMemberships(channels, userId, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.membership.MembershipInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeChannel = true, includeChannelCustom = true, includeChannelType = includeType))"
        )
    )
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
        includeType: Boolean = false,
    ): ManageMemberships

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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.membership.MembershipInclude] to easily create the desired configuration.
     */
    fun manageMemberships(
        channelsToSet: List<ChannelMembershipInput>,
        channelsToRemove: List<String>,
        userId: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        include: MembershipInclude = MembershipInclude()
    ): ManageMemberships

    /**
     * @see [PubNub.getChannelMembers]
     */
    @Deprecated(
        replaceWith =
            ReplaceWith(
                "getChannelMembers(channel = channel, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                    "includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use getChannelMembers instead",
    )
    fun getMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
    ): GetChannelMembers

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new getChannelMembers(channel, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "getChannelMembers(channel, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeUUIDType)"
        )
    )
    actual fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
        includeType: Boolean,
    ): GetChannelMembers

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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.member.MemberInclude] to easily create the desired configuration.
     */
    actual fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): GetChannelMembers

    /**
     * @see [PubNub.setChannelMembers]
     */
    @Deprecated(
        replaceWith =
            ReplaceWith(
                "setChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                    "includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use setChannelMembers instead",
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
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
    ): ManageChannelMembers

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new setChannelMembers(channel, userIds, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "setChannelMembers(channel, userIds, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeUUIDType)"
        )
    )
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
        includeType: Boolean,
    ): ManageChannelMembers

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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.member.MemberInclude] to easily create the desired configuration.
     */
    actual fun setChannelMembers(
        channel: String,
        users: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): ManageChannelMembers

    /**
     * @see [PubNub.removeChannelMembers]
     */
    @Deprecated(
        replaceWith =
            ReplaceWith(
                "removeChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                    "includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.WARNING,
        message = "Use removeChannelMembers instead",
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
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
    ): ManageChannelMembers

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
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new removeChannelMembers(channel, userIds, limit, page, filter, sort, MembershipInclude(...))",
        replaceWith = ReplaceWith(
            "removeChannelMembers(channel, userIds, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeUUIDType)"
        )
    )
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
        includeType: Boolean,
    ): ManageChannelMembers

    /**
     * Remove members from a Channel.
     *
     * @param channel Channel name
     * @param userIds Members to remove from channel.
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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.member.MemberInclude] to easily create the desired configuration.
     */
    actual fun removeChannelMembers(
        channel: String,
        userIds: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        include: MemberInclude
    ): ManageChannelMembers

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
     * @param includeType Include the type field for UUID metadata
     */
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This function is deprecated. Use the new manageChannelMembers(channels, uuidsToSet, uuidsToRemove, limit, page, filter, sort, MemberInclude(...))",
        replaceWith = ReplaceWith(
            "manageChannelMembers(channels, uuidsToSet, uuidsToRemove, limit, page, filter, sort, com.pubnub.api.models.consumer.objects.member.MemberInclude(includeTotalCount = includeCount, includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeUserType = includeUUIDType)"
        )
    )
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
        includeUUIDType: Boolean = false,
    ): ManageChannelMembers

    /**
     * Set or remove members in a channel.
     *
     * @param channel Channel name
     * @param usersToSet Collection of members to add to the channel. @see [com.pubnub.api.models.consumer.objects.member.PNMember.Partial]
     * @param userIdsToRemove Members to remove from channel.
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
     * @param include Request specific elements to be available in response.
     * Use [com.pubnub.api.models.consumer.objects.member.MemberInclude] to easily create the desired configuration.
     */
    fun manageChannelMembers(
        channel: String,
        usersToSet: Collection<MemberInput>,
        userIdsToRemove: Collection<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        include: MemberInclude = MemberInclude()
    ): ManageChannelMembers

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
     * @param customMessageType The custom type associated with the message.
     */
    actual fun sendFile(
        channel: String,
        fileName: String,
        inputStream: InputStream,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?,
        customMessageType: String?,
    ): SendFile

    /**
     * Retrieve list of files uploaded to Channel.
     *
     * @param channel Channel name
     * @param limit Number of files to return. Minimum value is 1, and maximum is 100. Default value is 100.
     * @param next Previously-returned cursor bookmark for fetching the next page. @see [PNPage.PNNext]
     */
    actual fun listFiles(
        channel: String,
        limit: Int?,
        next: PNPage.PNNext?,
    ): ListFiles

    /**
     * Generate URL which can be used to download file from target Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     */
    actual fun getFileUrl(
        channel: String,
        fileName: String,
        fileId: String,
    ): GetFileUrl

    /**
     * Download file from specified Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     * @param cipherKey Key to be used to decrypt downloaded data. If a key is not provided,
     *                  the SDK uses the cipherKey from the @see [PNConfiguration].
     */
    actual fun downloadFile(
        channel: String,
        fileName: String,
        fileId: String,
        cipherKey: String?,
    ): DownloadFile

    /**
     * Delete file from specified Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     */
    actual fun deleteFile(
        channel: String,
        fileName: String,
        fileId: String,
    ): DeleteFile

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
     * @param customMessageType The custom type associated with the message.
     */
    actual fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        customMessageType: String?,
    ): PublishFileMessage

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
     * This can be achieved by setting [PNConfiguration.retryConfiguration] when initializing the client.
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     * @param withPresence Also subscribe to related presence channel.
     * @param withTimetoken A timetoken to start the subscribe loop from.
     */
    actual fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long,
    )

    /**
     * When subscribed to a single channel, this function causes the client to issue a leave from the channel
     * and close any open socket to the PubNub Network.
     *
     * For multiplexed channels, the specified channel(s) will be removed and the socket remains open
     * until there are no more channels remaining in the list.
     *
     * **WARNING**
     * Unsubscribing from all the channel(s) and then subscribing to a new channel Y isn't the same as
     * Subscribing to channel Y and then unsubscribing from the previously subscribed channel(s).
     *
     * Unsubscribing from all the channels resets the timetoken and thus,
     * there could be some gaps in the subscriptions that may lead to a message loss.
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    actual fun unsubscribe(
        channels: List<String>,
        channelGroups: List<String>,
    )
}
