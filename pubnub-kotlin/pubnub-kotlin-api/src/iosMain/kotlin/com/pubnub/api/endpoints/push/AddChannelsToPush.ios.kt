package com.pubnub.api.endpoints.push

import cocoapods.PubNubSwift.PubNubObjC
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.toNSData
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
actual interface AddChannelsToPush : Endpoint<PNPushAddChannelResult>

@OptIn(ExperimentalForeignApi::class)
open class AddChannelsToPushImpl(
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
        } ?: run {
            callback.accept(Result.failure(PubNubException("Cannot create NSData from $deviceId")))
        }
    }
}