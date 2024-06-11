package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.addChannelsToPushNotificationsWithChannels
import com.pubnub.kmp.PNFuture
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.toNSData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
actual interface AddChannelsToPush : PNFuture<PNPushAddChannelResult>

@OptIn(ExperimentalForeignApi::class)
class AddChannelsToPushImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val deviceId: String
): AddChannelsToPush {
    override fun async(callback: Consumer<Result<PNPushAddChannelResult>>) {
        deviceId.toNSData()?.let { data: NSData ->
            pubnub.addChannelsToPushNotificationsWithChannels(
                channels = channels,
                deviceId = data,
                onSuccess = callback.onSuccessHandler { PNPushAddChannelResult() },
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
    }
}