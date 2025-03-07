package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.addChannelsToPushNotificationsWithChannels
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
actual interface AddChannelsToPush : PNFuture<PNPushAddChannelResult>

@OptIn(ExperimentalForeignApi::class)
class AddChannelsToPushImpl(
    private val pushType: PNPushType,
    private val pubnub: KMPPubNub,
    private val channels: List<String>,
    private val deviceId: String,
    private val topic: String?,
    private val environment: PNPushEnvironment
) : AddChannelsToPush {
    override fun async(callback: Consumer<Result<PNPushAddChannelResult>>) {
        pubnub.addChannelsToPushNotificationsWithChannels(
            channels = channels,
            deviceId = deviceId,
            pushType = pushType.toParamString(),
            topic = topic.orEmpty(),
            environment = environment.toParamString(),
            onSuccess = callback.onSuccessHandler { PNPushAddChannelResult() },
            onFailure = callback.onFailureHandler()
        )
    }
}
