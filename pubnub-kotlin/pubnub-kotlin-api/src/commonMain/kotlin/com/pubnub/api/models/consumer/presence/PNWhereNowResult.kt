package com.pubnub.api.models.consumer.presence

/**
 * Result of the [com.pubnub.api.PubNub.whereNow] operation.
 *
 * @property channels List of channels where a UUID is present.
 */
class PNWhereNowResult(
    val channels: List<String>,
)
