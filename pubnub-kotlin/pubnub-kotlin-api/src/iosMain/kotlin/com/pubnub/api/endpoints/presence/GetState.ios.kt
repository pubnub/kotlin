package com.pubnub.api.endpoints.presence

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getPresenceStateWithChannels
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.kmp.safeCast
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getPresenceState]
 */
actual interface GetState : PNFuture<PNGetStateResult>

@OptIn(ExperimentalForeignApi::class)
class GetStateImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val channelGroups: List<String>,
    private val uuid: String
) : GetState {
    override fun async(callback: Consumer<Result<PNGetStateResult>>) {
        pubnub.getPresenceStateWithChannels(
            channels = channels,
            channelGroups = channelGroups,
            uuid = uuid,
            onSuccess = callback.onSuccessHandler { PNGetStateResult(stateByUUID = it?.safeCast() ?: emptyMap()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
