package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeChannelsFromPushWithChannels
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.kmp.toNSData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
actual interface RemoveChannelsFromPush : PNFuture<PNPushRemoveChannelResult>

@OptIn(ExperimentalForeignApi::class)
class RemoveChannelsFromPushImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val deviceId: String,
    private val pushType: PNPushType,
    private val topic: String?,
    private val environment: PNPushEnvironment,
) : RemoveChannelsFromPush {
    override fun async(callback: Consumer<Result<PNPushRemoveChannelResult>>) {
        deviceId.toNSData()?.let { data: NSData ->
            pubnub.removeChannelsFromPushWithChannels(
                channels = channels,
                deviceId = data,
                pushType = pushType.toParamString(),
                topic = topic.orEmpty(),
                environment = environment.toParamString(),
                onSuccess = callback.onSuccessHandler { PNPushRemoveChannelResult() },
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
    }
}
