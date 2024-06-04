package com.pubnub.api.endpoints.presence

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getPresenceStateWithChannels
import com.pubnub.api.Endpoint
import com.pubnub.api.JsonElement
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getPresenceState]
 */
actual interface GetState : Endpoint<PNGetStateResult> {
}

@OptIn(ExperimentalForeignApi::class)
class GetStateImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val channelGroups: List<String>,
    private val uuid: String
): GetState {
    override fun async(callback: Consumer<Result<PNGetStateResult>>) {
        pubnub.getPresenceStateWithChannels(
            channels = channels,
            channelGroups = channelGroups,
            uuid = uuid,
            onSuccess = callback.onSuccessHandler { PNGetStateResult(stateByUUID = it as Map<String, JsonElement>) },
            onFailure = callback.onFailureHandler()
        )
    }
}