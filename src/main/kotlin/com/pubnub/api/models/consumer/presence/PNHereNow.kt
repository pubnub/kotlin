package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement

data class PNHereNowResult(
    val totalChannels: Int,
    val totalOccupancy: Int,
    var channels: HashMap<String, PNHereNowChannelData> = hashMapOf()
)

data class PNHereNowChannelData(
    val channelName: String,
    val occupancy: Int,
    var occupants: List<PNHereNowOccupantData> = emptyList()
)

data class PNHereNowOccupantData(
    val uuid: String,
    val state: JsonElement? = null
)
