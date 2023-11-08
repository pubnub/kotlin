package com.pubnub.api.models.consumer.history

import com.google.gson.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.endpoints.History

/**
 * Result of the [PubNub.history] operation.
 *
 * @property messages List of messages as instances of [PNHistoryItemResult].
 * @property startTimetoken Start timetoken of the returned list of messages.
 * @property endTimetoken End timetoken of the returned list of messages.
 */
class PNHistoryResult internal constructor(
    val messages: List<PNHistoryItemResult>,
    val startTimetoken: Long,
    val endTimetoken: Long
)

/**
 * Encapsulates a message in terms of a history entry.
 *
 * @property entry The actual message content.
 * @property timetoken Publish timetoken of the message, if requested via [History.includeTimetoken]
 * @property meta Metadata of the message, if requested via [History.includeMeta].
 * Is `null` if not requested, otherwise an empty string if requested but no associated metadata.
 * @property error The error associated with message retrieval, if any.
 * e.g. a message is unencrypted but PubNub instance is configured with the Crypto
 * so PubNub can't decrypt the unencrypted message and return the message.
 */
data class PNHistoryItemResult(
    val entry: JsonElement,
    val timetoken: Long? = null,
    val meta: JsonElement? = null,
    val error: PubNubError? = null
)
