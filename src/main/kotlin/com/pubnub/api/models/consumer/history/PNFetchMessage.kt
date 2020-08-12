package com.pubnub.api.models.consumer.history

import com.google.gson.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.FetchMessages

/**
 * Result of the [PubNub.fetchMessages] operation.
 *
 * @property channels Map of channels and their respective lists of [PNFetchMessageItem].
 */
class PNFetchMessagesResult(
    val channels: HashMap<String, List<PNFetchMessageItem>>
)

/**
 * Encapsulates a message in terms of a batch history entry.
 *
 * @property message The actual message content.
 * @property timetoken Publish timetoken of the message.
 * @property meta Metadata of the message, if requested via [FetchMessages.includeMeta].
 * Is `null` if not requested, otherwise an empty string if requested but no associated metadata.
 */
class PNFetchMessageItem(
    val message: JsonElement,
    val meta: JsonElement?,
    val timetoken: Long
) {
    /**
     * The message actions associated with the message.
     * Is `null` if not requested via [FetchMessages.includeMessageActions].
     * The key of the map is the action type. The value is another map,
     * which key is the actual value of the message action,
     * and the key being a list of actions, ie. a list of UUIDs which have posted such a message action.
     *
     * @see [Action]
     */
    var actions: Map<String, HashMap<String, List<Action>>>? = null
        internal set
}

/**
 * Encapsulates a message action in terms of batch history.
 *
 * @property uuid The UUID of the publisher.
 * @property actionTimetoken The publish timetoken of the message action.
 */
class Action(
    val uuid: String,
    val actionTimetoken: String
)
