package com.pubnub.api.endpoints

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.messageCountsFor
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.messageCounts]
 */
actual interface MessageCounts : PNFuture<PNMessageCountResult>

@OptIn(ExperimentalForeignApi::class)
class MessageCountsImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val channelsTimetoken: List<Long>
): MessageCounts {
    override fun async(callback: Consumer<Result<PNMessageCountResult>>) {
        pubnub.messageCountsFor(
            channels = channels,
            channelsTimetokens = channelsTimetoken,
            onSuccess = callback.onSuccessHandler { PNMessageCountResult(it?.channels() as Map<String, Long>) },
            onFailure = callback.onFailureHandler()
        )
    }
}