package com.pubnub.kmp

import ObjectsResponse
import Partial
import com.pubnub.api.Endpoint
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.v2.PNConfiguration
import toOptional
import kotlin.js.Promise
import PubNub as PubNubJs

class PubNub(override val configuration: PNConfiguration) : CommonPubNub {

    private val jsPubNub: PubNubJs = PubNubJs(configuration.toJs())

    override fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?
    ): Endpoint<PNPublishResult> {
        return object : Endpoint<PNPublishResult> {
            override fun async(action: (Result<PNPublishResult>) -> Unit) {
                val params = object : PubNubJs.PublishParameters {
                    override var message: Any = "myMessage"
                    override var channel: String = "myChannel"
                }
                jsPubNub.publish(params).then(onFulfilled = { it: PubNubJs.PublishResponse ->
                    action(Result.success(PNPublishResult(it.timetoken.toString().toLong())))
                }, onRejected = { it: Throwable ->
                    action(Result.failure(it))
                })
            }
        }
    }

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
    override fun fire(
        channel: String,
        message: Any,
        meta: Any?,
        usePost: Boolean,
        ttl: Int?
    ): Endpoint<PNPublishResult> {
        TODO("Not yet implemented")
    }

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
    override fun signal(channel: String, message: Any): Endpoint<PNPublishResult> {
        TODO("Not yet implemented")
    }

    /**
     * Queries the local subscribe loop for channels currently in the mix.
     *
     * @return A list of channels the client is currently subscribed to.
     */
    override fun getSubscribedChannels(): List<String> {
        TODO("Not yet implemented")
    }

    /**
     * Queries the local subscribe loop for channel groups currently in the mix.
     *
     * @return A list of channel groups the client is currently subscribed to.
     */
    override fun getSubscribedChannelGroups(): List<String> {
        TODO("Not yet implemented")
    }

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
    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushAddChannelResult> {
        TODO("Not yet implemented")
    }

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
    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushListProvisionsResult> {
        TODO("Not yet implemented")
    }

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
    override fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushRemoveChannelResult> {
        TODO("Not yet implemented")
    }

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
    override fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): Endpoint<PNPushRemoveAllChannelsResult> {
        TODO("Not yet implemented")
    }

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
    override fun history(
        channel: String,
        start: Long?,
        end: Long?,
        count: Int,
        reverse: Boolean,
        includeTimetoken: Boolean,
        includeMeta: Boolean
    ): Endpoint<PNHistoryResult> {
        TODO("Not yet implemented")
    }

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
    override fun fetchMessages(
        channels: List<String>,
        maximumPerChannel: Int,
        start: Long?,
        end: Long?,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean
    ): Endpoint<PNFetchMessagesResult> {
        TODO("Not yet implemented")
    }

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
    override fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean
    ): Endpoint<PNFetchMessagesResult> {
        TODO("Not yet implemented")
    }

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
    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): Endpoint<PNDeleteMessagesResult> {
        TODO("Not yet implemented")
    }

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
    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): Endpoint<PNMessageCountResult> {
        TODO("Not yet implemented")
    }

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
    override fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean
    ): Endpoint<PNHereNowResult> {
        TODO("Not yet implemented")
    }

    /**
     * Obtain information about the current list of channels to which a UUID is subscribed to.
     *
     * @param uuid UUID of the user to get its current channel subscriptions. Defaults to the UUID of the client.
     * @see [PNConfiguration.uuid]
     */
    override fun whereNow(uuid: String): Endpoint<PNWhereNowResult> {
        TODO("Not yet implemented")
    }

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
    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
        uuid: String
    ): Endpoint<PNSetStateResult> {
        TODO("Not yet implemented")
    }

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
    override fun getPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        uuid: String
    ): Endpoint<PNGetStateResult> {
        TODO("Not yet implemented")
    }

    /**
     * Track the online and offline status of users and devices in real time and store custom state information.
     * When you have Presence enabled, PubNub automatically creates a presence channel for each channel.
     *
     * Subscribing to a presence channel or presence channel group will only return presence events
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
        TODO("Not yet implemented")
    }

    /**
     * Add an action on a published message. Returns the added action in the response.
     *
     * @param channel Channel to publish message actions to.
     * @param messageAction The message action object containing the message action's type,
     *                      value and the publish timetoken of the original message.
     */
    override fun addMessageAction(channel: String, messageAction: PNMessageAction): Endpoint<PNAddMessageActionResult> {
        TODO("Not yet implemented")
    }

    /**
     * Remove a previously added action on a published message. Returns an empty response.
     *
     * @param channel Channel to remove message actions from.
     * @param messageTimetoken The publish timetoken of the original message.
     * @param actionTimetoken The publish timetoken of the message action to be removed.
     */
    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): Endpoint<PNRemoveMessageActionResult> {
        TODO("Not yet implemented")
    }

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
    override fun getMessageActions(
        channel: String,
        start: Long?,
        end: Long?,
        limit: Int?
    ): Endpoint<PNGetMessageActionsResult> {
        TODO("Not yet implemented")
    }

    /**
     * Get a list of message actions in a channel. Returns a list of actions in the response.
     *
     * @param channel Channel to fetch message actions from.
     * @param page The paging object used for pagination. @see [PNBoundedPage]
     */
    override fun getMessageActions(channel: String, page: PNBoundedPage): Endpoint<PNGetMessageActionsResult> {
        TODO("Not yet implemented")
    }

    /**
     * Adds a channel to a channel group.
     *
     * @param channels The channels to add to the channel group.
     * @param channelGroup The channel group to add the channels to.
     */
    override fun addChannelsToChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): Endpoint<PNChannelGroupsAddChannelResult> {
        TODO("Not yet implemented")
    }

    /**
     * Lists all the channels of the channel group.
     *
     * @param channelGroup Channel group to fetch the belonging channels.
     */
    override fun listChannelsForChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsAllChannelsResult> {
        TODO("Not yet implemented")
    }

    /**
     * Removes channels from a channel group.
     *
     * @param channelGroup The channel group to remove channels from
     * @param channels The channels to remove from the channel group.
     */
    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): Endpoint<PNChannelGroupsRemoveChannelResult> {
        TODO("Not yet implemented")
    }

    /**
     * Lists all registered channel groups for the subscribe key.
     */
    override fun listAllChannelGroups(): Endpoint<PNChannelGroupsListAllResult> {
        TODO("Not yet implemented")
    }

    /**
     * Removes the channel group.
     *
     * @param channelGroup The channel group to remove.
     */
    override fun deleteChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsDeleteGroupResult> {
        TODO("Not yet implemented")
    }

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
    override fun grant(
        read: Boolean,
        write: Boolean,
        manage: Boolean,
        delete: Boolean,
        ttl: Int,
        authKeys: List<String>,
        channels: List<String>,
        channelGroups: List<String>,
        uuids: List<String>
    ): Endpoint<PNAccessManagerGrantResult> {
        TODO("Not yet implemented")
    }

    /**
     * See [grant]
     */
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
        uuids: List<String>
    ): Endpoint<PNAccessManagerGrantResult> {
        TODO("Not yet implemented")
    }

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
    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>
    ): Endpoint<PNGrantTokenResult> {
        TODO("Not yet implemented")
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
    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUserId: UserId?,
        spacesPermissions: List<SpacePermissions>,
        usersPermissions: List<UserPermissions>
    ): Endpoint<PNGrantTokenResult> {
        TODO("Not yet implemented")
    }

    /**
     * This method allows you to disable an existing token and revoke all permissions embedded within.
     *
     * @param token Existing token with embedded permissions.
     */
    override fun revokeToken(token: String): Endpoint<Unit> {
        TODO("Not yet implemented")
    }

    /**
     * Returns a 17 digit precision Unix epoch from the server.
     */
    override fun time(): Endpoint<PNTimeResult> {
        TODO("Not yet implemented")
    }

    fun setUserMetadata(
        uuid: String?,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Map<String, Any?>?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): Endpoint<PNUUIDMetadataResult> {
        return Endpoint({
            val params = object : PubNubJs.SetUUIDMetadataParameters {
                override var data: PubNubJs.UUIDMetadata = UUIDMetadata(
                    name.toOptional(),
                    externalId.toOptional(),
                    profileUrl.toOptional(),
                    email.toOptional(),
                    status.toOptional(),
                    type.toOptional(),
                    custom.toOptional()
                )

                override var uuid: String? = uuid

                override var include: PubNubJs.`T$30`? = object : PubNubJs.`T$30` {
                    override var customFields: Boolean? = includeCustom
                }
            }

            jsPubNub.objects.setUUIDMetadata(params)
        }) { it: ObjectsResponse<PubNubJs.UUIDMetadataObject> ->
            PNUUIDMetadataResult(
                it.status.toInt(),
                with(it.data) {
                    PNUUIDMetadata(
                        id,
                        name,
                        externalId,
                        profileUrl,
                        email,
                        custom,
                        updated,
                        eTag,
                        type,
                        status
                    )
                }
            )
        }
    }

    fun removeUserMetadata(uuid: String?): Endpoint<PNRemoveMetadataResult> {
        return Endpoint({
            jsPubNub.objects.removeUUIDMetadata(object : PubNubJs.RemoveUUIDMetadataParameters {
                override var uuid: String? = uuid
            })
        }) { response ->
            PNRemoveMetadataResult(response.status.toInt())
        }
    }

    fun getUserMetadata(
        uuid: String?,
        includeCustom: Boolean
    ): Endpoint<PNUUIDMetadataResult> {
        TODO("Not yet implemented")
    }

    fun getAllUserMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): Endpoint<PNUUIDMetadataArrayResult> {
        TODO("Not yet implemented")
    }

    override fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): Endpoint<PNChannelMetadataArrayResult> {
        TODO("Not yet implemented")
    }

    /**
     * Returns metadata for the specified Channel, optionally including the custom data object for each.
     *
     * @param channel Channel name.
     * @param includeCustom Include respective additional fields in the response.
     */
    override fun getChannelMetadata(
        channel: String,
        includeCustom: Boolean
    ): Endpoint<PNChannelMetadataResult> {
        TODO("Not yet implemented")
    }

    /**
     * Set metadata for a Channel in the database, optionally including the custom data object for each.
     *
     * @param channel Channel name.
     * @param name Name of a channel.
     * @param description Description of a channel.
     * @param custom Object with supported data types.
     * @param includeCustom Include respective additional fields in the response.
     */
    override fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): Endpoint<PNChannelMetadataResult> {
        TODO("Not yet implemented")
    }

    /**
     * Removes the metadata from a specified channel.
     *
     * @param channel Channel name.
     */
    override fun removeChannelMetadata(channel: String): Endpoint<PNRemoveMetadataResult> {
        TODO("Not yet implemented")
    }

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
    override fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): Endpoint<PNUUIDMetadataArrayResult> {
        TODO("Not yet implemented")
    }

    /**
     * Returns metadata for the specified UUID, optionally including the custom data object for each.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     * @param includeCustom Include respective additional fields in the response.
     */
    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): Endpoint<PNUUIDMetadataResult> {
        TODO("Not yet implemented")
    }

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
    override fun setUUIDMetadata(
        uuid: String?,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): Endpoint<PNUUIDMetadataResult> {
        TODO("Not yet implemented")
    }

    /**
     * Removes the metadata from a specified UUID.
     *
     * @param uuid Unique user identifier. If not supplied then current user’s uuid is used.
     */
    override fun removeUUIDMetadata(uuid: String?): Endpoint<PNRemoveMetadataResult> {
        TODO("Not yet implemented")
    }

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
    override fun getMemberships(
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

    /**
     * @see [PubNub.setMemberships]
     */
    override fun addMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

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
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

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
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

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
    ): Endpoint<PNChannelMembershipArrayResult> {
        TODO("Not yet implemented")
    }

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
    override fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

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
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

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
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

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
    ): Endpoint<PNMemberArrayResult> {
        TODO("Not yet implemented")
    }

    /**
     * Retrieve list of files uploaded to Channel.
     *
     * @param channel Channel name
     * @param limit Number of files to return. Minimum value is 1, and maximum is 100. Default value is 100.
     * @param next Previously-returned cursor bookmark for fetching the next page. @see [PNPage.PNNext]
     */
    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): Endpoint<PNListFilesResult> {
        TODO("Not yet implemented")
    }

    /**
     * Generate URL which can be used to download file from target Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     */
    override fun getFileUrl(channel: String, fileName: String, fileId: String): Endpoint<PNFileUrlResult> {
        TODO("Not yet implemented")
    }

    /**
     * Delete file from specified Channel.
     *
     * @param channel Name of channel to which the file has been uploaded.
     * @param fileName Name under which the uploaded file is stored.
     * @param fileId Unique identifier for the file, assigned during upload.
     */
    override fun deleteFile(channel: String, fileName: String, fileId: String): Endpoint<PNDeleteFileResult> {
        TODO("Not yet implemented")
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
    override fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?
    ): Endpoint<PNPublishFileMessageResult> {
        TODO("Not yet implemented")
    }

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
    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        TODO("Not yet implemented")
    }

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
    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
        TODO("Not yet implemented")
    }

    override fun setToken(token: String?) {
        TODO("Not yet implemented")
    }
}


private fun <T, U> Endpoint(promiseFactory: () -> Promise<T>, responseMapping: (T) -> U): Endpoint<U> =
    object : Endpoint<U> {
        override fun async(callback: (Result<U>) -> Unit) {
            promiseFactory().then(
                onFulfilled = { response: T ->
                    callback(Result.success(responseMapping(response)))
                },
                onRejected = { throwable ->
                    callback(Result.failure(throwable))
                }
            )
        }
    }

fun UUIDMetadata(
    name: Optional<String?>,
    externalId: Optional<String?>,
    profileUrl: Optional<String?>,
    email: Optional<String?>,
    status: Optional<String?>,
    type: Optional<String?>,
    custom: Optional<Map<String, Any?>?>
): PubNubJs.UUIDMetadata {
    val result: PubNubJs.UUIDMetadata = createJsObject()
    name.onValue { result.name = it }
    externalId.onValue { result.externalId = it }
    profileUrl.onValue { result.profileUrl = it }
    email.onValue { result.email = it }
    status.onValue { result.status = it }
    type.onValue { result.type = it }
    custom.onValue { result.custom = it?.toCustomObject() }
    return result
}

fun Map<String, Any?>.toCustomObject(): PubNubJs.CustomObject {
    val custom = Any().asDynamic()
    entries.forEach {
        custom[it.key] = it.value
    }
    @Suppress("UnsafeCastFromDynamic")
    return custom
}

fun <T : Partial> createJsObject(): T = Any().asDynamic() as T

fun PNConfiguration.toJs(): PubNubJs.PNConfiguration {
    val config: PubNubJs.PNConfiguration = createJsObject()
    config.userId = userId.value
//    config.subscribeKey = subscribeKey
//    config.publishKey = publishKey
//    config.cipherKey
//    config.authKey: String?
//    config.logVerbosity: Boolean?
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