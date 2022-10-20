package com.pubnub.api.coroutine

import com.pubnub.api.PNConfiguration
import com.pubnub.api.coroutine.internal.Subscribe
import com.pubnub.api.coroutine.model.FetchMessages
import com.pubnub.api.coroutine.model.MessageEvent
import com.pubnub.api.coroutine.model.PresenceEvent
import com.pubnub.api.coroutine.model.toFetchMessages
import com.pubnub.api.coroutine.model.toPresenceEvent
import com.pubnub.api.endpoints.DeleteMessagesImpl
import com.pubnub.api.endpoints.FetchMessagesImpl
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.MessageCountsImpl
import com.pubnub.api.endpoints.message_actions.AddMessageActionImpl
import com.pubnub.api.endpoints.message_actions.GetMessageActionsImpl
import com.pubnub.api.endpoints.message_actions.RemoveMessageActionImpl
import com.pubnub.api.endpoints.presence.HereNowImpl
import com.pubnub.api.endpoints.presence.WhereNowImpl
import com.pubnub.api.endpoints.pubsub.PublishImpl
import com.pubnub.api.endpoints.pubsub.SignalImpl
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PubNub(configuration: PNConfiguration) {

    private val oldPubNub = com.pubnub.api.PubNub(configuration)
    private val subscribe = Subscribe(oldPubNub)

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
    suspend fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ): Result<Long> = PublishImpl(
        pubnub = oldPubNub,
        channel = channel,
        message = message,
        meta = meta,
        shouldStore = shouldStore,
        usePost = usePost,
        replicate = replicate,
        ttl = ttl
    ).awaitResult().map { it.timetoken }

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
    suspend fun fire(
        channel: String,
        message: Any,
        meta: Any? = null,
        usePost: Boolean = false,
        ttl: Int? = null
    ): Result<Long> = publish(
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
    suspend fun signal(
        channel: String,
        message: Any
    ): Result<Long> =
        SignalImpl(pubnub = oldPubNub, channel = channel, message = message).awaitResult().map { it.timetoken }
    //endregion

    //region StoragePlayback

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
     */
    suspend fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage = PNBoundedPage(),
        includeUUID: Boolean = true,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false
    ): Result<FetchMessages> = FetchMessagesImpl(
        pubnub = oldPubNub,
        channels = channels,
        page = page,
        includeUUID = includeUUID,
        includeMeta = includeMeta,
        includeMessageActions = includeMessageActions
    ).awaitResult().map(PNFetchMessagesResult::toFetchMessages)

    fun messageFlow(vararg channels: String): Flow<MessageEvent> =
        subscribe.messageFlow(channels = channels.toList())

    fun presenceFlow(vararg channels: String): Flow<PresenceEvent> =
        subscribe.presenceFlow(channels = channels.toList()).map(PNPresenceEventResult::toPresenceEvent)

    //region MessageActions
    /**
     * Add an action on a published message. Returns the added action in the response.
     *
     * @param channel Channel to publish message actions to.
     * @param messageAction The message action object containing the message action's type,
     *                      value and the publish timetoken of the original message.
     */
    suspend fun addMessageAction(channel: String, messageAction: PNMessageAction): Result<PNAddMessageActionResult> =
        AddMessageActionImpl(pubnub = oldPubNub, channel = channel, messageAction = messageAction).awaitResult()

    /**
     * Remove a previously added action on a published message. Returns an empty response.
     *
     * @param channel Channel to remove message actions from.
     * @param messageTimetoken The publish timetoken of the original message.
     * @param actionTimetoken The publish timetoken of the message action to be removed.
     */
    suspend fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): Result<PNRemoveMessageActionResult> =
        RemoveMessageActionImpl(
            pubnub = oldPubNub,
            channel = channel,
            messageTimetoken = messageTimetoken,
            actionTimetoken = actionTimetoken
        ).awaitResult()

    /**
     * Get a list of message actions in a channel. Returns a list of actions in the response.
     *
     * @param channel Channel to fetch message actions from.
     * @param page The paging object used for pagination. @see [PNBoundedPage]
     */
    suspend fun getMessageActions(
        channel: String,
        page: PNBoundedPage = PNBoundedPage()
    ): Result<PNGetMessageActionsResult> =
        GetMessageActionsImpl(pubnub = oldPubNub, channel = channel, page = page).awaitResult()
    //endregion

    fun messageActionFlow(vararg channels: String): Flow<PNMessageActionResult> =
        subscribe.messageActionFlow(channels = channels.toList())

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
    suspend fun deleteMessages(
        channels: List<String>,
        start: Long? = null,
        end: Long? = null
    ): Result<Unit> =
        DeleteMessagesImpl(pubnub = oldPubNub, channels = channels, start = start, end = end).awaitResult().map { }

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
    suspend fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): Result<PNMessageCountResult> =
        MessageCountsImpl(pubnub = oldPubNub, channels = channels, channelsTimetoken = channelsTimetoken).awaitResult()
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
    suspend fun hereNow(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        includeState: Boolean = false,
        includeUUIDs: Boolean = true
    ): Result<PNHereNowResult> = HereNowImpl(
        pubnub = oldPubNub,
        channels = channels,
        channelGroups = channelGroups,
        includeState = includeState,
        includeUUIDs = includeUUIDs
    ).awaitResult()

    /**
     * Obtain information about the current list of channels to which a UUID is subscribed to.
     *
     * @param uuid UUID of the user to get its current channel subscriptions. Defaults to the UUID of the client.
     * @see [PNConfiguration.uuid]
     */
    suspend fun whereNow(uuid: String = oldPubNub.configuration.userId.value): Result<PNWhereNowResult> = WhereNowImpl(
        pubnub = oldPubNub, uuid = uuid
    ).awaitResult()
    //endregion
}
