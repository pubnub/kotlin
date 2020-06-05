package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement

class PNHereNowResult(
    val totalChannels: Int,
    val totalOccupancy: Int,
    val channels: HashMap<String, PNHereNowChannelData> = hashMapOf()
)

class PNHereNowChannelData(
    val channelName: String,
    val occupancy: Int,
    var occupants: List<PNHereNowOccupantData> = emptyList()
)

class PNHereNowOccupantData(
    val uuid: String,
    val state: JsonElement? = null
)
