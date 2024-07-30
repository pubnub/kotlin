package com.pubnub.api.models.consumer.presence

import com.pubnub.api.JsonElement

/**
 * Result of the HereNow operation.
 *
 * @property totalChannels Total number channels matching the associated HereNow call.
 * @property totalOccupancy Total occupancy matching the associated HereNow call.
 * @property channels A map with values of [PNHereNowChannelData] for each channel.
 */
class PNHereNowResult(
    val totalChannels: Int,
    val totalOccupancy: Int,
    val channels: Map<String, PNHereNowChannelData>,
)

/**
 * Wrapper class representing 'here now' data for a given channel.
 *
 * @property channelName The channel name.
 * @property occupancy Total number of UUIDs currently in the channel.
 * @property occupants List of [PNHereNowOccupantData] (users) currently in the channel.
 */
class PNHereNowChannelData(
    val channelName: String,
    val occupancy: Int,
    val occupants: List<PNHereNowOccupantData>,
)

/**
 * Wrapper class representing a UUID (user) within the means of HereNow calls.
 *
 * @property uuid UUID of the user if requested via HereNow.includeUUIDs.
 * @property state Presence State of the user if requested via HereNow.includeState.
 */
class PNHereNowOccupantData(
    val uuid: String,
    val state: JsonElement? = null,
)
