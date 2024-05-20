package com.pubnub.api.endpoints.presence

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.JsonElement
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.kmp.toMap

class HereNowImpl(pubnub: PubNub, params: PubNub.HereNowParameters) : HereNow, EndpointImpl<PubNub.HereNowResponse, PNHereNowResult>(
    promiseFactory = { pubnub.hereNow(params) },
    responseMapping = { hereNowResponse ->
        PNHereNowResult(
            hereNowResponse.totalChannels.toInt(),
            hereNowResponse.totalOccupancy.toInt(),
            hereNowResponse.channels.toMap().mapValues {
                val data = it.value
                PNHereNowChannelData(
                    data.name,
                    data.occupancy.toInt(),
                    data.occupants.map { occupant ->
                        PNHereNowOccupantData(
                            occupant.uuid,
                            JsonElementImpl(occupant.state),
                        )
                    }
                )
            }.toMutableMap()
        )
    }
)