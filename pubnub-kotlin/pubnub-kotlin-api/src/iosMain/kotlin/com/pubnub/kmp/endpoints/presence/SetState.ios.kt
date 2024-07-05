package com.pubnub.kmp.endpoints.presence

import cocoapods.PubNubSwift.AnyJSONObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.setPresenceStateWithChannels
import com.pubnub.api.JsonElementImpl
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.endpoints.presence.SetState
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class SetStateImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val channelGroups: List<String>,
    private val state: Any,
): SetState {
    override fun async(callback: Consumer<Result<PNSetStateResult>>) {
        pubnub.setPresenceStateWithChannels(
            channels = channels,
            channelGroups = channelGroups,
            state = AnyJSONObjC(value = state),
            onSuccess = callback.onSuccessHandler { PNSetStateResult(state = JsonElementImpl(it)) },
            onFailure = callback.onFailureHandler()
        )
    }
}