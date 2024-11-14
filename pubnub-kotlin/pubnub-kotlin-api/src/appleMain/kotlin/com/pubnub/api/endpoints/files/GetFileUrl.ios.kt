package com.pubnub.api.endpoints.files

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.getFileUrlWithChannel
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.getFileUrl]
 */
actual interface GetFileUrl : PNFuture<PNFileUrlResult>

@OptIn(ExperimentalForeignApi::class)
class GetFileUrlImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val fileName: String,
    private val fileId: String
) : GetFileUrl {
    override fun async(callback: Consumer<Result<PNFileUrlResult>>) {
        pubnub.getFileUrlWithChannel(
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            onSuccess = callback.onSuccessHandler { PNFileUrlResult(url = it.orEmpty()) },
            onFailure = callback.onFailureHandler()
        )
    }
}
