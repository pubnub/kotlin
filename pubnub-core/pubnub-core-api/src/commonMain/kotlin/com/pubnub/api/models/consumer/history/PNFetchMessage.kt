package com.pubnub.api.models.consumer.history

import com.pubnub.api.JsonValue
import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessageItem.Action

/**
 * Result of the FetchMessages operation.
 *
 * @property channels Map of channels and their respective lists of [PNFetchMessageItem].
 */
data class PNFetchMessagesResult(
    val channels: Map<String, List<PNFetchMessageItem>>,
    val page: PNBoundedPage?,
)

/**
 * Encapsulates a message in terms of a batch history entry.
 *
 * @property uuid Publisher uuid. Is `null` if not requested.
 * @property message The actual message content.
 * @property timetoken Publish timetoken of the message.
 * @property meta Metadata of the message, if requested via `includeMeta`.
 * Is `null` if not requested, otherwise an empty string if requested but no associated metadata.
 * @property actions The message actions associated with the message.
 * Is `null` if not requested via `includeMessageActions`.
 * The key of the map is the action type. The value is another map,
 * which key is the actual value of the message action,
 * and the key being a list of actions, i.e. a list of UUIDs which have posted such a message action.
 * @see [Action]
 * @property messageType The message type associated with the item.
 * @property error The error associated with message retrieval, if any.
 * e.g. a message is unencrypted but PubNub instance is configured with the Crypto
 * so PubNub can't decrypt the unencrypted message and return the message.
 */
data class PNFetchMessageItem(
    val uuid: String?,
    val message: JsonValue,
    val meta: JsonValue?,
    val timetoken: Long?,
    val actions: Map<String, Map<String, List<Action>>>? = null,
    val messageType: HistoryMessageType?,
    val error: PubNubError? = null,
) {
    // for compatibility with legacy Java SDK
    class Action(uuid: String, actionTimetoken: String) :
        com.pubnub.api.models.consumer.history.Action(uuid, actionTimetoken)
}

/**
 * Encapsulates a message action in terms of batch history.
 *
 * @property uuid The UUID of the publisher.
 * @property actionTimetoken The publish timetoken of the message action.
 */
open class Action(
    val uuid: String,
    val actionTimetoken: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Action) {
            return false
        }

        if (uuid != other.uuid) {
            return false
        }
        if (actionTimetoken != other.actionTimetoken) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + actionTimetoken.hashCode()
        return result
    }
}
