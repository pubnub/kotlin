package com.pubnub.docs.presence

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.v2.callbacks.Result

class HereNowOther {
    private fun hereNowReturningState(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#returning-state

        // snippet.hereNowReturningState
        pubnub.hereNow(
            channels = listOf("ch1", "ch2"),
            includeState = true
        ).async { result: Result<PNHereNowResult> ->
            result.onSuccess { res: PNHereNowResult ->
                res.channels.values.forEach { channelData ->
                    channelData.channelName // ch1
                    channelData.occupancy // 3
                    channelData.occupants.forEach { o ->
                        o.uuid // some_uuid, returned by default
                        o.state // {"data":{"isTyping":true}}, requested
                    }
                }
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end
    }

    private fun hereNowReturnOccupancyOnly(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#return-occupancy-only

        // snippet.hereNowReturnOccupancyOnly
        pubnub.hereNow(
            channels = listOf("my_channel"), // who is present on those channels?
            includeUUIDs = false, // if false, only shows occupancy count
            includeState = false // include state with request (false by default)
        ).async { result: Result<PNHereNowResult> ->
            result.onSuccess { res: PNHereNowResult ->
                res.channels.values.forEach { channelData ->
                    channelData.channelName // ch1
                    channelData.occupancy // 3
                }
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end
    }

    private fun hereNowForChannelGroups(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#here-now-for-channel-groups

        // snippet.hereNowForChannelGroups
        pubnub.hereNow(
            channelGroups = listOf("cg1", "cg2", "cg3"), // who is present on those channels groups
            includeState = true, // include state with request (false by default)
            includeUUIDs = true // if false, only shows occupancy count
        ).async { result: Result<PNHereNowResult> ->
            result.onSuccess { res: PNHereNowResult ->
                res.totalOccupancy
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end
    }
}
