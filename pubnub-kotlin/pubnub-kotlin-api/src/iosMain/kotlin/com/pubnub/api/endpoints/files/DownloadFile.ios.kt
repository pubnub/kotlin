package com.pubnub.api.endpoints.files

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.downloadFileWithChannel
import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.DownloadableImpl
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSInputStream

/**
 * @see [PubNub.sendFile]
 */
actual interface DownloadFile : PNFuture<PNDownloadFileResult>

@OptIn(ExperimentalForeignApi::class)
class DownloadFileImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val fileName: String,
    private val fileId: String
) : DownloadFile {
    override fun async(callback: Consumer<Result<PNDownloadFileResult>>) {
        pubnub.downloadFileWithChannel(
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            onSuccess = callback.onSuccessHandler {
                PNDownloadFileResult(
                    fileName = it?.name().orEmpty(),
                    byteStream = it?.url()?.let { url -> DownloadableImpl(inputStream = NSInputStream(uRL = url)) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
