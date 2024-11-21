package com.pubnub.api.java

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.java.builder.PresenceBuilder
import com.pubnub.api.java.builder.SubscribeBuilder
import com.pubnub.api.java.builder.UnsubscribeBuilder
import com.pubnub.api.java.callbacks.SubscribeCallback
import com.pubnub.api.java.endpoints.DeleteMessages
import com.pubnub.api.java.endpoints.FetchMessages
import com.pubnub.api.java.endpoints.History
import com.pubnub.api.java.endpoints.MessageCounts
import com.pubnub.api.java.endpoints.access.Grant
import com.pubnub.api.java.endpoints.access.RevokeToken
import com.pubnub.api.java.endpoints.access.builder.GrantTokenBuilder
import com.pubnub.api.java.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.java.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.java.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.java.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.java.endpoints.files.DeleteFile
import com.pubnub.api.java.endpoints.files.DownloadFile
import com.pubnub.api.java.endpoints.files.GetFileUrl
import com.pubnub.api.java.endpoints.files.ListFiles
import com.pubnub.api.java.endpoints.files.PublishFileMessage
import com.pubnub.api.java.endpoints.files.SendFile
import com.pubnub.api.java.endpoints.message_actions.AddMessageAction
import com.pubnub.api.java.endpoints.message_actions.GetMessageActions
import com.pubnub.api.java.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.java.endpoints.objects_api.channel.GetAllChannelsMetadata
import com.pubnub.api.java.endpoints.objects_api.channel.GetChannelMetadata
import com.pubnub.api.java.endpoints.objects_api.channel.RemoveChannelMetadata
import com.pubnub.api.java.endpoints.objects_api.channel.SetChannelMetadata
import com.pubnub.api.java.endpoints.objects_api.members.GetChannelMembers
import com.pubnub.api.java.endpoints.objects_api.members.ManageChannelMembers
import com.pubnub.api.java.endpoints.objects_api.members.RemoveChannelMembers
import com.pubnub.api.java.endpoints.objects_api.members.SetChannelMembers
import com.pubnub.api.java.endpoints.objects_api.memberships.GetMemberships
import com.pubnub.api.java.endpoints.objects_api.memberships.ManageMemberships
import com.pubnub.api.java.endpoints.objects_api.memberships.RemoveMemberships
import com.pubnub.api.java.endpoints.objects_api.memberships.SetMemberships
import com.pubnub.api.java.endpoints.objects_api.memberships.SetMembershipsBuilder
import com.pubnub.api.java.endpoints.objects_api.uuid.GetAllUUIDMetadata
import com.pubnub.api.java.endpoints.objects_api.uuid.GetUUIDMetadata
import com.pubnub.api.java.endpoints.objects_api.uuid.RemoveUUIDMetadata
import com.pubnub.api.java.endpoints.objects_api.uuid.SetUUIDMetadata
import com.pubnub.api.java.endpoints.presence.GetState
import com.pubnub.api.java.endpoints.presence.HereNow
import com.pubnub.api.java.endpoints.presence.SetState
import com.pubnub.api.java.endpoints.presence.WhereNow
import com.pubnub.api.java.endpoints.pubsub.Publish
import com.pubnub.api.java.endpoints.pubsub.Signal
import com.pubnub.api.java.endpoints.push.AddChannelsToPush
import com.pubnub.api.java.endpoints.push.ListPushProvisions
import com.pubnub.api.java.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.java.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership
import com.pubnub.api.java.v2.PNConfiguration
import com.pubnub.api.java.v2.callbacks.EventEmitter
import com.pubnub.api.java.v2.callbacks.StatusEmitter
import com.pubnub.api.java.v2.endpoints.pubsub.PublishBuilder
import com.pubnub.api.java.v2.endpoints.pubsub.SignalBuilder
import com.pubnub.api.java.v2.entities.Channel
import com.pubnub.api.java.v2.entities.ChannelGroup
import com.pubnub.api.java.v2.entities.ChannelMetadata
import com.pubnub.api.java.v2.entities.UserMetadata
import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import java.io.InputStream
import java.util.UUID

interface PubNub : EventEmitter, StatusEmitter {
    val timestamp: Int
    val baseUrl: String

    /**
     * The current version of the PubNub SDK.
     */
    val version: String

    /**
     * Get the configuration that was used to initialize this PubNub instance.
     * Modifying the values in this configuration is not advised, as it may lead
     * to undefined behavior.
     */
    val configuration: com.pubnub.api.v2.PNConfiguration

    /**
     * Causes the client to create an open TCP socket to the PubNub Real-Time Network and begin listening for messages
     * on a specified channel.
     *
     * To subscribe to a channel the client must send the appropriate [PNConfiguration.setSubscribeKey] at
     * initialization.
     *
     * By default, a newly subscribed client will only receive messages published to the channel
     * after the `subscribe()` call completes.
     *
     * If a client gets disconnected from a channel, it can automatically attempt to reconnect to that channel
     * and retrieve any available messages that were missed during that period.
     * This can be achieved by setting [PNConfiguration.setRetryConfiguration] when initializing the client.
     *
     */
    fun subscribe(): SubscribeBuilder

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
     */
    fun unsubscribe(): UnsubscribeBuilder

    /**
     * Track the online and offline status of users and devices in real time and store custom state information.
     * When you have Presence enabled, PubNub automatically creates a presence channel for each channel.
     *
     * Subscribing to a presence channel or presence channel group will only return presence events
     */
    fun presence(): PresenceBuilder

    /**
     * Enable push notifications on provided set of channels.
     */
    fun addPushNotificationsOnChannels(): AddChannelsToPush

    /**
     * Disable push notifications on provided set of channels.
     */
    fun removePushNotificationsFromChannels(): RemoveChannelsFromPush

    /**
     * Disable push notifications from all channels registered with the specified [RemoveAllPushChannelsForDevice.deviceId].
     */
    fun removeAllPushNotificationsFromDeviceWithPushToken(): RemoveAllPushChannelsForDevice

    /**
     * Request a list of all channels on which push notifications have been enabled using specified [ListPushProvisions.deviceId].
     */
    fun auditPushChannelProvisions(): ListPushProvisions

    /**
     * Obtain information about the current list of channels to which a UUID is subscribed to.
     */
    fun whereNow(): WhereNow

    /**
     * Obtain information about the current state of a channel including a list of unique user IDs
     * currently subscribed to the channel and the total occupancy count of the channel.
     */
    fun hereNow(): HereNow

    /**
     * Returns a 17 digit precision Unix epoch from the server.
     */
    fun time(): Time

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
     */
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "Use fetchMessages() instead",
    )
    fun history(): History

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
     */
    fun fetchMessages(): FetchMessages

    /**
     * Removes messages from the history of a specific channel.
     *
     * NOTE: There is a setting to accept delete from history requests for a key,
     * which you must enable by checking the Enable `Delete-From-History` checkbox
     * in the key settings for your key in the Administration Portal.
     *
     * Requires Initialization with secret key.
     *
     */
    fun deleteMessages(): DeleteMessages

    /**
     * Fetches the number of messages published on one or more channels since a given time.
     * The count returned is the number of messages in history with a timetoken value greater
     * than the passed value in the [MessageCounts.channelsTimetoken] parameter.
     *
     */
    fun messageCounts(): MessageCounts

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
     */
    fun grant(): Grant

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
     */
    fun grantToken(): GrantTokenBuilder

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
     */
    fun grantToken(ttl: Int): GrantTokenBuilder

    /**
     * This method allows you to disable an existing token and revoke all permissions embedded within.
     */
    fun revokeToken(): RevokeToken

    /**
     * Retrieve state information specific to a subscriber UUID.
     *
     * State information is supplied as a JSON object of key/value pairs.
     */
    fun getPresenceState(): GetState

    /**
     * Set state information specific to a subscriber UUID.
     *
     * State information is supplied as a JSON object of key/value pairs.
     *
     * If [PNConfiguration.setMaintainPresenceState] is `true`, and the `uuid` matches [PNConfiguration.getUuid], the
     * state for channels will be saved in the PubNub client and resent with every heartbeat and initial subscribe
     * request.
     * In that case, it's not recommended to mix setting state through channels *and* channel groups, as state set
     * through the channel group will be overwritten after the next heartbeat or subscribe reconnection (e.g. after loss
     * of network).
     *
     */
    fun setPresenceState(): SetState

    /**
     * Send a message to all subscribers of a channel.
     *
     * To publish a message you must first specify a valid [PNConfiguration.setPublishKey].
     * A successfully published message is replicated across the PubNub Real-Time Network and sent
     * simultaneously to all subscribed clients on a channel.
     *
     * Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting
     * [PNConfiguration.setSecure] to `true` during initialization.
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
     * @param message The payload
     * @param channel The channel to publish message to.
     *
     * @return [PublishBuilder]
     */
    fun publish(message: Any, channel: String): PublishBuilder

    /**
     * Send a message to all subscribers of a channel.
     *
     * To publish a message you must first specify a valid [PNConfiguration.setPublishKey].
     * A successfully published message is replicated across the PubNub Real-Time Network and sent
     * simultaneously to all subscribed clients on a channel.
     *
     * Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting
     * [PNConfiguration.setSecure] to `true` during initialization.
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
     */
    @Deprecated(
        replaceWith =
            ReplaceWith(
                "publish(message, channel)"
            ),
        level = DeprecationLevel.WARNING,
        message = "Use publish(Object, String) instead",
    )
    fun publish(): Publish

    /**
     * Send a signal to all subscribers of a channel.
     *
     * By default, signals are limited to a message payload size of 30 bytes.
     * This limit applies only to the payload, and not to the URI or headers.
     * If you require a larger payload size, please [contact support](mailto:support@pubnub.com).
     *
     * @param message The payload
     * @param channel The channel to signal message to.
     *
     * @return [SignalBuilder]
     */
    fun signal(message: Any, channel: String): SignalBuilder

    /**
     * Send a signal to all subscribers of a channel.
     *
     * By default, signals are limited to a message payload size of 30 bytes.
     * This limit applies only to the payload, and not to the URI or headers.
     * If you require a larger payload size, please [contact support](mailto:support@pubnub.com).
     */
    @Deprecated(
        replaceWith =
            ReplaceWith(
                "signal(message, channel)"
            ),
        level = DeprecationLevel.WARNING,
        message = "Use signal(Object, String) instead",
    )
    fun signal(): Signal

    /**
     * Lists all registered channel groups for the subscribe key.
     */
    fun listAllChannelGroups(): ListAllChannelGroup

    /**
     * Lists all the channels of the channel group.
     */
    fun listChannelsForChannelGroup(): AllChannelsChannelGroup

    /**
     * Adds a channel to a channel group.
     */
    fun addChannelsToChannelGroup(): AddChannelChannelGroup

    /**
     * Removes channels from a channel group.
     */
    fun removeChannelsFromChannelGroup(): RemoveChannelChannelGroup

    /**
     * Removes the channel group.
     */
    fun deleteChannelGroup(): DeleteChannelGroup

    /**
     * Set metadata for a UUID in the database, optionally including the custom data object for each.
     */
    fun setUUIDMetadata(): SetUUIDMetadata

    /**
     * Returns a paginated list of UUID Metadata objects, optionally including the custom data object for each.
     */
    fun getAllUUIDMetadata(): GetAllUUIDMetadata

    /**
     * Returns metadata for the specified UUID, optionally including the custom data object for each.
     */
    fun getUUIDMetadata(): GetUUIDMetadata

    /**
     * Removes the metadata from a specified UUID.
     */
    fun removeUUIDMetadata(): RemoveUUIDMetadata

    /**
     * Set metadata for a Channel in the database, optionally including the custom data object for each.
     */
    fun setChannelMetadata(): SetChannelMetadata.Builder

    /**
     * Returns a paginated list of Channel Metadata objects, optionally including the custom data object for each.
     */
    fun getAllChannelsMetadata(): GetAllChannelsMetadata

    /**
     * Returns metadata for the specified Channel, optionally including the custom data object for each.
     */
    fun getChannelMetadata(): GetChannelMetadata.Builder

    /**
     * Removes the metadata from a specified channel.
     */
    fun removeChannelMetadata(): RemoveChannelMetadata.Builder

    /**
     * The method returns a list of channel memberships for a user. This method doesn't return a user's subscriptions.
     */
    fun getMemberships(): GetMemberships

    /**
     * Set channel memberships for a UUID.
     */
    fun setMemberships(): SetMemberships.Builder // add deprecation

    // todo add kDoc
    fun setMemberships(channelMemberships: Collection<PNChannelMembership>): SetMembershipsBuilder

    /**
     * Remove channel memberships for a UUID.
     */
    fun removeMemberships(): RemoveMemberships.Builder

    /**
     * Add and remove channel memberships for a UUID.
     */
    fun manageMemberships(): ManageMemberships.Builder

    /**
     * The method returns a list of members in a channel. The list will include user metadata for members
     * that have additional metadata stored in the database.
     */
    fun getChannelMembers(): GetChannelMembers.Builder

    /**
     * This method sets members in a channel.
     */
    fun setChannelMembers(): SetChannelMembers.Builder

    /**
     * Remove members from a Channel.
     */
    fun removeChannelMembers(): RemoveChannelMembers.Builder

    /**
     * Set or remove members in a channel.
     */
    fun manageChannelMembers(): ManageChannelMembers.Builder

    /**
     * Add an action on a published message. Returns the added action in the response.
     */
    fun addMessageAction(): AddMessageAction

    /**
     * Get a list of message actions in a channel. Returns a list of actions in the response.
     */
    fun getMessageActions(): GetMessageActions

    /**
     * Remove a previously added action on a published message. Returns an empty response.
     */
    fun removeMessageAction(): RemoveMessageAction

    /**
     * Upload file / data to specified Channel.
     */
    fun sendFile(): SendFile.Builder

    /**
     * Retrieve list of files uploaded to Channel.
     */
    fun listFiles(): ListFiles.Builder

    /**
     * Generate URL which can be used to download file from target Channel.
     *
     */
    fun getFileUrl(): GetFileUrl.Builder

    /**
     * Download file from specified Channel.
     */
    fun downloadFile(): DownloadFile.Builder

    /**
     * Delete file from specified Channel.
     */
    fun deleteFile(): DeleteFile.Builder

    /**
     * Publish file message from specified Channel.
     */
    fun publishFileMessage(): PublishFileMessage.Builder

    fun reconnect()

    /**
     * Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status]
     *
     * @param timetoken optional timetoken to use for the subscriptions on reconnection.
     */
    fun reconnect(timetoken: Long = 0L)

    /**
     * Send a message to PubNub Functions Event Handlers.
     *
     * These messages will go directly to any Event Handlers registered on the channel that you fire to
     * and will trigger their execution. The content of the fired request will be available for processing
     * within the Event Handler.
     *
     * The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel.
     * The message is also not stored in history.
     */
    fun fire(message: Any, channel: String): PublishBuilder

    /**
     * Send a message to PubNub Functions Event Handlers.
     *
     * These messages will go directly to any Event Handlers registered on the channel that you fire to
     * and will trigger their execution. The content of the fired request will be available for processing
     * within the Event Handler.
     *
     * The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel.
     * The message is also not stored in history.
     */
    @Deprecated(
        replaceWith =
            ReplaceWith(
                "publish(message, channel)"
            ),
        level = DeprecationLevel.WARNING,
        message = "Use publish(Object, String) instead",
    )
    fun fire(): Publish

    /**
     * Queries the local subscribe loop for channels currently in the mix.
     *
     * @return A list of channels the client is currently subscribed to.
     */
    fun getSubscribedChannels(): List<String>

    /**
     * Queries the local subscribe loop for channel groups currently in the mix.
     *
     * @return A list of channel groups the client is currently subscribed to.
     */
    fun getSubscribedChannelGroups(): List<String>

    fun channel(name: String): Channel

    fun channelGroup(name: String): ChannelGroup

    fun channelMetadata(id: String): ChannelMetadata

    fun userMetadata(id: String): UserMetadata

    fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet

    /**
     * Add a legacy listener for both client status and events.
     * Prefer `addListener(EventListener)` and `addListener(StatusListener)` if possible.
     *
     * @param listener The listener to be added.
     */
    fun addListener(listener: SubscribeCallback)

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
        cipherKey: String?,
    ): String

    /**
     * Perform Cryptographic decryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     *
     * @return InputStream containing the encryption of `inputStream` using [PNConfiguration.getCipherKey]
     * @throws PubNubException Throws exception in case of failed decryption.
     */
    @Throws(PubNubException::class)
    fun decryptInputStream(inputStream: InputStream): InputStream

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
        cipherKey: String?,
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
        cipherKey: String?,
    ): String

    /**
     * Perform Cryptographic encryption of an input string and a cipher key.
     *
     * @param inputString String to be encrypted.
     *
     * @return String containing the encryption of `inputString` using [PNConfiguration.getCipherKey].
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    @Throws(PubNubException::class)
    fun encrypt(inputString: String): String

    /**
     * Perform Cryptographic encryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for encryption.
     *
     * @return InputStream containing the encryption of `inputStream` using [PNConfiguration.getCipherKey].
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    @Throws(PubNubException::class)
    fun encryptInputStream(inputStream: InputStream): InputStream

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
        cipherKey: String?,
    ): InputStream

    @Throws(PubNubException::class)
    fun parseToken(token: String): PNToken

    /**
     * Update the authorization token granted by the server.
     */
    fun setToken(token: String?)

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    fun disconnect()

    /**
     * Unsubscribe from all channels and all channel groups
     */
    fun unsubscribeAll()

    /**
     * Frees up threads eventually and allows for a clean exit.
     */
    fun destroy()

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
                "com.pubnub.internal.java.PubNubForJavaImpl",
            ).getConstructor(PNConfiguration::class.java).newInstance(configuration) as PubNub
        }

        /**
         * Generates random UUID to use. You should set a unique UUID to identify the user or the device
         * that connects to PubNub.
         */
        @JvmStatic
        fun generateUUID(): String = "pn-${UUID.randomUUID()}"
    }
}
