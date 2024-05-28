package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeChannelsFromPushWithChannels
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.toNSData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
actual interface RemoveChannelsFromPush : Endpoint<PNPushRemoveChannelResult>
@OptIn(ExperimentalForeignApi::class)
class RemoveChannelsFromPushImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val deviceId: String,
    private val pushType: PNPushType
): RemoveChannelsFromPush {
    override fun async(callback: Consumer<Result<PNPushRemoveChannelResult>>) {
        deviceId.toNSData()?.let { data: NSData ->
            pubnub.removeChannelsFromPushWithChannels(
                channels = channels,
                deviceId = data,
                pushType = pushType.toParamString(),
                onSuccess = callback.onSuccessHandler { PNPushRemoveChannelResult() },
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
    }
}