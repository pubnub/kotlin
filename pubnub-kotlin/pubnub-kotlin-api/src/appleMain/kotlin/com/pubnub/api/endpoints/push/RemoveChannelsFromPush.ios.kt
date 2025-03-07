package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.removeChannelsFromPushWithChannels
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
actual interface RemoveChannelsFromPush : PNFuture<PNPushRemoveChannelResult>

@OptIn(ExperimentalForeignApi::class)
class RemoveChannelsFromPushImpl(
    private val pubnub: KMPPubNub,
    private val channels: List<String>,
    private val deviceId: String,
    private val pushType: PNPushType,
    private val topic: String?,
    private val environment: PNPushEnvironment,
) : RemoveChannelsFromPush {
    override fun async(callback: Consumer<Result<PNPushRemoveChannelResult>>) {
        pubnub.removeChannelsFromPushWithChannels(
            channels = channels,
            deviceId = deviceId,
            pushType = pushType.toParamString(),
            topic = topic.orEmpty(),
            environment = environment.toParamString(),
            onSuccess = callback.onSuccessHandler { PNPushRemoveChannelResult() },
            onFailure = callback.onFailureHandler()
        )
    }
}
