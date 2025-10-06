package com.pubnub.api.endpoints.presence

import cocoapods.PubNubSwift.KMPHereNowChannelData
import cocoapods.PubNubSwift.KMPHereNowOccupantData
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.hereNowWithChannels
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.kmp.safeCast
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.hereNow]
 */
actual interface HereNow : PNFuture<PNHereNowResult>

@OptIn(ExperimentalForeignApi::class)
class HereNowImpl(
    private val pubnub: KMPPubNub,
    private val channels: List<String>,
    private val channelGroups: List<String>,
    private val includeState: Boolean,
    private val includeUUIDs: Boolean,
    private val limit: Int = 1000,
    private val offset: Int? = null,
) : HereNow {
    override fun async(callback: Consumer<Result<PNHereNowResult>>) {
        pubnub.hereNowWithChannels(
            channels = channels,
            channelGroups = channelGroups,
            includeState = includeState,
            includeUUIDs = includeUUIDs,
            // todo pass limit and offset once available
            // limit = limit,
            // offset = offset,
            onSuccess = callback.onSuccessHandler {
                PNHereNowResult(
                    totalChannels = it?.totalChannels()?.toInt() ?: 0,
                    totalOccupancy = it?.totalOccupancy()?.toInt() ?: 0,
                    // nextOffset = it?.nextOffset()?.toInt(), // todo uncomment once available
                    channels = (it?.channels()?.safeCast<String, KMPHereNowChannelData>())?.mapValues { entry ->
                        PNHereNowChannelData(
                            channelName = entry.value.channelName(),
                            occupancy = entry.value.occupancy().toInt(),
                            occupants = (entry.value.occupants().filterIsInstance<KMPHereNowOccupantData>()).map { occupant ->
                                PNHereNowOccupantData(
                                    uuid = occupant.uuid(),
                                    state = JsonElementImpl(occupant.state())
                                )
                            }
                        )
                    }?.toMutableMap() ?: emptyMap<String, PNHereNowChannelData>().toMutableMap()
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
